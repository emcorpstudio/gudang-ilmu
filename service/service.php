<?php
    date_default_timezone_set("Asia/Jakarta");
    include("config.php");
    session_start();
    ob_start();
    $key = "aplikasi-gudang-ilmu";
    $response = array();
    if (isset($_POST['function'])){
        $function = $_POST['function'];
        $userkey = $_POST['key'];
        if($userkey==$key){
            if($function=="Login"){
                $email = $_POST['email'];
                $password = md5($_POST['password']);
				$sql = "SELECT * FROM tbl_user WHERE email = '$email' AND password = '$password'";
                $result = mysqli_query($db,$sql);
                $row = mysqli_fetch_array($result,MYSQLI_ASSOC);
                $count = mysqli_num_rows($result);
                if($count == 1) {
                    $response["success"] = 1;
                    $response["message"] = "Login berhasil";
                    $response["id_user"] = $row["id_user"];
                    $response["full_name"] = $row["full_name"];
                    $response["address"] = $row["address"];
                    $response["phone"] = $row["phone"];
                    $response["email"] = $row["email"];
                    $response["identity"] = $row["identity"];
                    $response["status"] = $row["status"];
                    $response["role"] = $row["role"];
                    $response["photo"] = $row["photo"];
                }else {
                    $response["success"] = 0;
                    $response["message"] = "Login gagal, periksa email / password anda!";
                }
                $result = array();
                $result['hasil'] = $response;
                print(json_encode($result));
            }else if($function=="Register"){
            	$id_user = $_POST['id_user'];
                $full_name = $_POST['full_name'];
                $address = $_POST['address'];
            	$phone = $_POST['phone'];
                $email = $_POST['email'];
                $password = md5($_POST['password']);
                $status = $_POST['status'];
                $role = $_POST['role'];
                if($id_user == ""){
                    //Insert
                    //Cek apakah email/nik sudah terdaftar
                    $sqlCek = "SELECT * FROM tbl_user WHERE email = '$email' OR phone = '$phone'";
                 	$resultCek = mysqli_query($db,$sqlCek);
                    $rowCek = mysqli_fetch_array($resultCek,MYSQLI_ASSOC);
                    $countCek = mysqli_num_rows($resultCek);
    				if($countCek<=0){
    					//Belum Terdaftar
    					$sql = "INSERT INTO tbl_user (full_name, address, phone, email, password, role, status) VALUES ('$full_name','$address','$phone','$email','$password','$role','$status')";
    	                $result = mysqli_query($db,$sql);
    	                if($result){
    	                    $response["message"] = 'Registrasi anda berhasil!';
    	                    $response["success"] = 1;
    	                }else{
    	                    $response["message"] = 'Registrasi anda gagal!';
    	                    $response["success"] = 0;
    	                }
    				}else{
    					//Sudah Terdaftar
    					$response["message"] = 'Registrasi anda gagal! Email atau nomor HP anda sudah terdaftar';
    					$response["success"] = 0;
    				}
                }else if($id_user != ""){
                    //Update
                    //Cek apakah email/nik sudah terdaftar
                    $sqlCek = "SELECT * FROM tbl_user WHERE (email = '$email' OR nik = '$nik' OR hp = '$hp') AND id_user != '$id_user'";
                    $resultCek = mysqli_query($db,$sqlCek);
                    $rowCek = mysqli_fetch_array($resultCek,MYSQLI_ASSOC);
                    $countCek = mysqli_num_rows($resultCek);
                    if($countCek<=0){
                        $sql = "";
                        if($_POST['password'] != ""){
                            //Update + password
                            $sql = "UPDATE tbl_user SET nama = '$nama',nik = '$nik',email = '$email',password = '$password',status = '$status' WHERE id_user = '$id_user'";
                        }else{
                            //Update tanpa password
                            $sql = "UPDATE tbl_user SET nama = '$nama',nik = '$nik',email = '$email',status = '$status' WHERE id_user = '$id_user'";
                        }
                        $result = mysqli_query($db,$sql);
                        if($result){
                            $response["message"] = 'Update data berhasil!';
                            $response["success"] = 1;
                        }else{
                            $response["message"] = 'Update data gagal!';
                            $response["success"] = 0;
                        }
                    }else{
                        //Sudah Terdaftar
                        $response["message"] = 'Update data gagal! Email, NIK atau nomor HP sudah terdaftar pada akun lain';
                        $response["success"] = 0;
                    }
                }
                $result = array();
                $result['result'] = $response;
                print(json_encode($result));
			}else if($function=="ListHome"){
                $id_user = $_POST['id_user'];
                $sql = "SELECT d.*,u.full_name FROM tbl_donasi d 
                                LEFT JOIN tbl_user u ON u.id_user = d.id_user
                            WHERE
                                d.status = 'ACTIVE'
                            ORDER BY
                                d.created_at DESC";
                if ($result=mysqli_query($db,$sql)){
                    while ($row=mysqli_fetch_array($result,MYSQLI_ASSOC)){
                        $listIsi = array();
                        $listIsi['id_donasi'] = $row['id_donasi'];
                        $listIsi['id_user'] = $row['id_user'];
                        $listIsi['judul'] = $row['judul'];
                        $listIsi['deskripsi'] = $row['deskripsi'];
                        $listIsi['foto'] = $row['foto'];
                        $listIsi["status"] = $row["status"];
                        $listIsi["full_name"] = $row["full_name"];
                        // $listIsi["created_at"] = date_format(date_create($row["created_at"]),"d-M-Y");
                        //Total Sumbangan
                        $sql = "SELECT SUM(amount) as jumlah FROM tbl_sumbang WHERE id_donasi = '$row[id_donasi]'";
                        $result_sql = mysqli_query($db,$sql);
                        $row = mysqli_fetch_array($result_sql,MYSQLI_ASSOC);
                        if(is_null($row['jumlah'])){
                            $listIsi["created_at"] = 'Rp 0';    
                        }else{
                            $listIsi["created_at"] = 'Rp '.number_format($row['jumlah']);
                        }
                        $response[] = $listIsi;
                    }
                    mysqli_free_result($result);
                }
                $result = array();
                $result['donasi'] = $response;
                //TopUp
                $sql = "SELECT SUM(amount) as jumlah FROM tbl_topup WHERE id_user = '$id_user'";
                $result_sql = mysqli_query($db,$sql);
                $row = mysqli_fetch_array($result_sql,MYSQLI_ASSOC);
                $topup = $row['jumlah'];
                //Sumbang Donasi
                $sql = "SELECT SUM(amount) as jumlah FROM tbl_sumbang WHERE id_user = '$id_user'";
                $result_sql = mysqli_query($db,$sql);
                $row = mysqli_fetch_array($result_sql,MYSQLI_ASSOC);
                $sumbang_donasi = 0;
                if(!is_null($row['jumlah'])){
                    $sumbang_donasi = $row['jumlah'];
                }
                //Sumbang Donasi Video
                $sumbang_donasi_video = 0;
                $sql = "SELECT SUM(amount) as jumlah FROM tbl_sumbang_video WHERE id_user = '$id_user'";
                $result_sql = mysqli_query($db,$sql);
                $row = mysqli_fetch_array($result_sql,MYSQLI_ASSOC);
                $sumbang_donasi = 0;
                if(!is_null($row['jumlah'])){
                    $sumbang_donasi_video = $row['jumlah'];
                }
                //Belanja
                $belanja = 0;
                $result['saldo'] = array(array('saldo'=> ($topup-($sumbang_donasi+$belanja+$sumbang_donasi_video))));
                print(json_encode($result));
            }else if($function=="ListDonasi"){
                $id_user = $_POST['id_user'];
                $sql = "";
                if($id_user != ""){
                    $sql = "SELECT d.*,u.full_name FROM tbl_donasi d 
                                LEFT JOIN tbl_user u ON u.id_user = d.id_user
                            WHERE
                                d.id_user = '$id_user'
                            ORDER BY
                                d.created_at DESC";
                }else{
                    $sql = "SELECT d.*,u.full_name FROM tbl_donasi d 
                                LEFT JOIN tbl_user u ON u.id_user = d.id_user
                            ORDER BY
                                d.created_at DESC";
                }
                if ($result=mysqli_query($db,$sql)){
                    while ($row=mysqli_fetch_array($result,MYSQLI_ASSOC)){
                        $listIsi = array();
                        $listIsi['id_donasi'] = $row['id_donasi'];
                        $listIsi['id_user'] = $row['id_user'];
                        $listIsi['judul'] = $row['judul'];
                        $listIsi['deskripsi'] = $row['deskripsi'];
                        $listIsi['foto'] = $row['foto'];
                        $listIsi["status"] = $row["status"];
                        // $listIsi["created_at"] = date_format(date_create($row["created_at"]),"d-M-Y");
                        $listIsi["full_name"] = $row["full_name"];

                        //Total Sumbangan
                        $sql = "SELECT SUM(amount) as jumlah FROM tbl_sumbang WHERE id_donasi = '$row[id_donasi]'";
                        $result_sql = mysqli_query($db,$sql);
                        $row = mysqli_fetch_array($result_sql,MYSQLI_ASSOC);
                        if(is_null($row['jumlah'])){
                            $listIsi["created_at"] = 'Rp 0';    
                        }else{
                            $listIsi["created_at"] = 'Rp '.number_format($row['jumlah']);
                        }
                        
                        $response[] = $listIsi;
                    }
                    mysqli_free_result($result);
                }
                $result = array();
                $result['hasil'] = $response;
                print(json_encode($result));
            }else if($function=="TopUp"){
                $id_user = $_POST['id_user'];
                $amount = $_POST['amount'];
                $type = $_POST['type'];
                $sql = "INSERT INTO tbl_topup (id_user,amount,type) VALUES ('$id_user','$amount','$type')";
                $result = mysqli_query($db,$sql);
                if($result){
                    $response["message"] = 'TopUp anda berhasil!';
                    $response["success"] = 1;
                }else{
                    $response["message"] = 'TopUp anda gagal!';
                    $response["success"] = 0;
                }
                $result = array();
                $result['result'] = $response;
                print(json_encode($result));
            }else if($function=="Donasi"){
                $id_user = $_POST['id_user'];
                $amount = $_POST['amount'];
                $id_donasi = $_POST['id_donasi'];
                $sql = "INSERT INTO tbl_sumbang (id_user,amount,id_donasi) VALUES ('$id_user','$amount','$id_donasi')";
                $result = mysqli_query($db,$sql);
                if($result){
                    $response["message"] = 'Donasi anda berhasil!';
                    $response["success"] = 1;
                }else{
                    $response["message"] = 'Donasi anda gagal!';
                    $response["success"] = 0;
                }
                $result = array();
                $result['result'] = $response;
                print(json_encode($result));
            }else if($function=="DonasiVideo"){
                $id_user = $_POST['id_user'];
                $amount = $_POST['amount'];
                $id_donasi = $_POST['id_donasi'];
                $sql = "INSERT INTO tbl_sumbang_video (id_user,amount,id_donasi) VALUES ('$id_user','$amount','$id_donasi')";
                $result = mysqli_query($db,$sql);
                if($result){
                    $response["message"] = 'Donasi anda berhasil!';
                    $response["success"] = 1;
                }else{
                    $response["message"] = 'Donasi anda gagal!';
                    $response["success"] = 0;
                }
                $result = array();
                $result['result'] = $response;
                print(json_encode($result));
            }else if($function=="AddDonasi"){
                $id_user = $_POST['id_user'];
                $judul = $_POST['judul'];
                $deskripsi = $_POST['deskripsi'];
                $foto = $_POST["foto"];

                $nama_foto = ($foto == "" ? "" : "img_foto_".date('YmdHis').".jpg");
                $path = "../file/donasi/";
                if($foto!=""){
                    file_put_contents($path.$nama_foto,base64_decode($foto));
                }
                //Insert
                $sql = "INSERT INTO tbl_donasi (id_user,judul,deskripsi,foto) VALUES ('$id_user','$judul','$deskripsi','$nama_foto')";
                $result = mysqli_query($db,$sql);
                if($result){
                    $response["message"] = 'Tambah donasi berhasil';
                    $response["success"] = 1;
                }else{
                    $response["message"] = 'Tambah donasi gagal';
                    $response["success"] = 0;
                }
                $result = array();
                $result['result'] = $response;
                print(json_encode($result));
            }else if($function=="ListProduk"){
                $id_user = $_POST['id_user'];
                $sql = "";
                if($id_user != ""){
                    $sql = "SELECT d.*,u.full_name FROM tbl_produk d 
                                LEFT JOIN tbl_user u ON u.id_user = d.id_user
                            WHERE
                                d.id_user = '$id_user'
                            ORDER BY
                                d.created_at DESC";
                }else{
                    $sql = "SELECT d.*,u.full_name FROM tbl_produk d 
                                LEFT JOIN tbl_user u ON u.id_user = d.id_user
                            ORDER BY
                                d.created_at DESC";
                }
                if ($result=mysqli_query($db,$sql)){
                    while ($row=mysqli_fetch_array($result,MYSQLI_ASSOC)){
                        $listIsi = array();
                        $listIsi['id_produk'] = $row['id_produk'];
                        $listIsi['id_user'] = $row['id_user'];
                        $listIsi['judul'] = $row['judul'];
                        $listIsi['deskripsi'] = $row['deskripsi'];
                        $listIsi['foto'] = $row['foto'];
                        $listIsi["status"] = $row["status"];
                        $listIsi["harga"] = number_format($row["harga"]);
                        $listIsi["created_at"] = date_format(date_create($row["created_at"]),"d-M-Y");
                        $listIsi["full_name"] = $row["full_name"];
                        $response[] = $listIsi;
                    }
                    mysqli_free_result($result);
                }
                $result = array();
                $result['hasil'] = $response;
                print(json_encode($result));
            }else if($function=="AddProduk"){
                $id_user = $_POST['id_user'];
                $judul = $_POST['judul'];
                $deskripsi = $_POST['deskripsi'];
                $harga = $_POST['harga'];
                $foto = $_POST["foto"];

                $nama_foto = ($foto == "" ? "" : "img_foto_".date('YmdHis').".jpg");
                $path = "../file/produk/";
                if($foto!=""){
                    file_put_contents($path.$nama_foto,base64_decode($foto));
                }
                //Insert
                $sql = "INSERT INTO tbl_produk (id_user,judul,deskripsi,foto,harga) VALUES ('$id_user','$judul','$deskripsi','$nama_foto','$harga')";
                $result = mysqli_query($db,$sql);
                if($result){
                    $response["message"] = 'Tambah produk berhasil';
                    $response["success"] = 1;
                }else{
                    $response["message"] = 'Tambah produk gagal';
                    $response["success"] = 0;
                }
                $result = array();
                $result['result'] = $response;
                print(json_encode($result));
            }else if($function=="ListVideo"){
                $id_user = $_POST['id_user'];
                $sql = "";
                if($id_user != ""){
                    $sql = "SELECT d.*,u.full_name FROM tbl_video d 
                                LEFT JOIN tbl_user u ON u.id_user = d.id_user
                            WHERE
                                d.id_user = '$id_user'
                            ORDER BY
                                d.created_at DESC";
                }else{
                    $sql = "SELECT d.*,u.full_name FROM tbl_video d 
                                LEFT JOIN tbl_user u ON u.id_user = d.id_user
                            ORDER BY
                                d.created_at DESC";
                }
                if ($result=mysqli_query($db,$sql)){
                    while ($row=mysqli_fetch_array($result,MYSQLI_ASSOC)){
                        $listIsi = array();
                        $listIsi['id_video'] = $row['id_video'];
                        $listIsi['id_user'] = $row['id_user'];
                        $listIsi['judul'] = $row['judul'];
                        $listIsi['deskripsi'] = $row['deskripsi'];
                        $listIsi['foto'] = $row['foto'];
                        $listIsi['video'] = $row['video'];
                        $listIsi["status"] = $row["status"];
                        $listIsi["created_at"] = date_format(date_create($row["created_at"]),"d-M-Y");
                        $listIsi["full_name"] = $row["full_name"];
                        $response[] = $listIsi;
                    }
                    mysqli_free_result($result);
                }
                $result = array();
                $result['hasil'] = $response;
                print(json_encode($result));
            }else if($function=="ListUser"){
				$role = $_POST['role'];
				$sql = "SELECT * FROM tbl_user WHERE role = '$role' ORDER BY nama ASC";
				if ($result=mysqli_query($db,$sql)){
					while ($row=mysqli_fetch_array($result,MYSQLI_ASSOC)){
                        $listIsi = array();
						$listIsi['id_user'] = $row['id_user'];
						$listIsi['nama'] = $row['nama'];
						$listIsi['hp'] = $row['hp'];
						$listIsi['email'] = $row['email'];
						$listIsi['nik'] = $row['nik'];
						$listIsi['role'] = $row['role'];
                        $listIsi["hp"] = $row["hp"];
                        $listIsi["status"] = $row["status"];
                        $response[] = $listIsi;
                    }
					mysqli_free_result($result);
				}
				$result = array();
                $result['hasil'] = $response;
                print(json_encode($result));
			}else if($function=="ListPengajuan"){
                $id_user = $_POST['id_user'];
                $sql = "";
                if($id_user != ""){
                    $sql = "SELECT * FROM tbl_pengajuan WHERE id_user = '$id_user' ORDER BY tgl_pengajuan ASC";
                }else{
                    $sql = "SELECT * FROM tbl_pengajuan ORDER BY tgl_pengajuan ASC";
                }
                if ($result=mysqli_query($db,$sql)){
                    while ($row=mysqli_fetch_array($result,MYSQLI_ASSOC)){
                        $listIsi = array();
                        $listIsi['id_pengajuan'] = $row['id_pengajuan'];
                        $listIsi['id_user'] = $row['id_user'];
                        $listIsi['jenis_pengajuan'] = $row['jenis_pengajuan'];
                        $listIsi['jenis_permohonan'] = $row['jenis_permohonan'];
                        $listIsi['nomor_kk'] = $row['nomor_kk'];
                        $listIsi['nik'] = $row['nik'];
                        $listIsi["nama_lengkap"] = $row["nama_lengkap"];
                        $listIsi["nama_kepala"] = $row["nama_kepala"];
                        $listIsi["alamat"] = $row["alamat"];
                        $listIsi["foto"] = $row["foto"];
                        $listIsi["ttd"] = $row["ttd"];
                        $listIsi["tgl_pengajuan"] = $row["tgl_pengajuan"];
                        $listIsi["status"] = $row["status"];
                        $listIsi["catatan"] = $row["catatan"];
                        $response[] = $listIsi;
                    }
                    mysqli_free_result($result);
                }
                $result = array();
                $result['hasil'] = $response;
                print(json_encode($result));
            }else if($function=="Pengajuan"){
                $id_pengajuan = $_POST['id_pengajuan'];
                $id_user = $_POST['id_user'];
                $jenis_pengajuan = $_POST['jenis_pengajuan'];
                $jenis_permohonan = $_POST['jenis_permohonan'];
                $nomor_kk = $_POST['nomor_kk'];
                $nik = $_POST['nik'];
                $nama_lengkap = $_POST["nama_lengkap"];
                $nama_kepala = $_POST["nama_kepala"];
                $alamat = $_POST["alamat"];
                $foto = $_POST["foto"];
                $ttd = $_POST["ttd"];

                $nama_foto = ($foto == "" ? "" : "img_foto_".date('YmdHis').".jpg");
                $nama_foto_ttd = ($ttd == "" ? "" : "img_ttd_".date('YmdHis').".jpg");
                $path = "images/";
                if($foto!=""){
                    file_put_contents($path.$nama_foto,base64_decode($foto));
                }
                if($ttd!=""){
                    file_put_contents($path.$nama_foto_ttd,base64_decode($ttd));
                }

                //Insert
                $sql = "INSERT INTO tbl_pengajuan (id_user,jenis_pengajuan,jenis_permohonan,nomor_kk,nik,nama_lengkap,nama_kepala,alamat,foto,ttd,tgl_pengajuan,status,catatan) VALUES ('$id_user','$jenis_pengajuan','$jenis_permohonan','$nomor_kk','$nik','$nama_lengkap','$nama_kepala','$alamat','$nama_foto','$nama_foto_ttd',NOW(),'WAITING','')";
                $result = mysqli_query($db,$sql);
                if($result){
                    $response["message"] = 'Pengajuan anda berhasil!';
                    $response["success"] = 1;
                }else{
                    $response["message"] = 'Pengajuan anda gagal!';
                    $response["success"] = 0;
                }
                $result = array();
                $result['result'] = $response;
                print(json_encode($result));
            }else if($function=="UpdatePengajuan"){
                $id_pengajuan = $_POST['id_pengajuan'];
                $id_user = $_POST['id_user'];
                $jenis_pengajuan = $_POST['jenis_pengajuan'];
                $jenis_permohonan = $_POST['jenis_permohonan'];
                $nomor_kk = $_POST['nomor_kk'];
                $nik = $_POST['nik'];
                $nama_lengkap = $_POST["nama_lengkap"];
                $nama_kepala = $_POST["nama_kepala"];
                $alamat = $_POST["alamat"];
                $foto = $_POST["foto"];
                $ttd = $_POST["ttd"];

                $nama_foto = ($foto == "" ? "" : "img_foto_".date('YmdHis').".jpg");
                $nama_foto_ttd = ($ttd == "" ? "" : "img_ttd_".date('YmdHis').".jpg");
                $path = "images/";
                if($foto!=""){
                    file_put_contents($path.$nama_foto,base64_decode($foto));
                    $sql = "UPDATE tbl_pengajuan SET foto = '$nama_foto' WHERE id_pengajuan = '$id_pengajuan'";
                    $result = mysqli_query($db,$sql);
                }
                if($ttd!=""){
                    file_put_contents($path.$nama_foto_ttd,base64_decode($ttd));
                    $sql = "UPDATE tbl_pengajuan SET ttd = '$nama_foto_ttd' WHERE id_pengajuan = '$id_pengajuan'";
                    $result = mysqli_query($db,$sql);
                }

                //Update Foto
                $sql = "UPDATE tbl_pengajuan SET jenis_permohonan = '$jenis_permohonan',nomor_kk = '$nomor_kk',nik = '$nik',nama_lengkap = '$nama_lengkap',nama_kepala = '$nama_kepala',alamat = '$alamat' WHERE id_pengajuan = '$id_pengajuan'";
                $result = mysqli_query($db,$sql);
                if($result){
                    $response["message"] = 'Update pengajuan anda berhasil!';
                    $response["success"] = 1;
                }else{
                    $response["message"] = 'Update pengajuan anda gagal!';
                    $response["success"] = 0;
                }
                $result = array();
                $result['result'] = $response;
                print(json_encode($result));
            }else if($function=="ApprovalPengajuan"){
                $id_pengajuan = $_POST['id_pengajuan'];
                $status = $_POST['status'];
                $sql = "UPDATE tbl_pengajuan SET status = '$status' WHERE id_pengajuan = '$id_pengajuan'";
                $result = mysqli_query($db,$sql);
                if($result){
                    $response["message"] = 'Update status pengajuan berhasil!';
                    $response["success"] = 1;
                    $sql = "SELECT u.id_push FROM tbl_pengajuan p LEFT JOIN tbl_user u ON u.id_user = p.id_user WHERE p.id_pengajuan = '$id_pengajuan'";
                    $result = mysqli_query($db,$sql);
                    $row = mysqli_fetch_array($result,MYSQLI_ASSOC);
                    $id_push = $row['id_push'];
                    sendMessage("Pengajuan anda telah di ".$status,$id_push);
                }else{
                    $response["message"] = 'Update status pengajuan gagal!';
                    $response["success"] = 0;
                }
                $result = array();
                $result['result'] = $response;
                print(json_encode($result));
            }
        }else{
            // no function
            $response["success"] = 0;
            $response["message"] = "Function missing 3";
            print(json_encode($response));    
        } 
    }else{
        // no function
        $response["success"] = 0;
        $response["message"] = "Function missing";
        print(json_encode($response));
    }

    function sendMessage($message,$id){
        $content = array("en" => $message);
        $fields = array(
            'app_id' => "893652a9-2d14-4c4c-86a9-d4b1d1e91399",
            'include_player_ids' => array($id),
            'data' => array("foo" => "bar"),
            'contents' => $content
        );
        $fields = json_encode($fields);

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, "https://onesignal.com/api/v1/notifications");
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json; charset=utf-8',
                                                   'Authorization: Basic ZjliYWQ1ZTItYzBkNC00NmM5LWJjMjUtZjk1ZjNlM2M5MmI2'));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($ch, CURLOPT_HEADER, FALSE);
        curl_setopt($ch, CURLOPT_POST, TRUE);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $fields);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);    

        $response = curl_exec($ch);
        curl_close($ch);
    
        $return["allresponses"] = $response;
        $return = json_encode( $return);
        // print("\n\nJSON received:\n");
        // print($return);
    }
?> 