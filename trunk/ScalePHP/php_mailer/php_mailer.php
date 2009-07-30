<?php
require_once("../lib/PHPMailer/class.phpmailer.php");
require_once("../lib/PHPMailer/class.smtp.php");
include "../lib/ObjToXML.php";
//$_POST["address"]="everest1573@gmail.com";
//$_POST["Subject"]="试验!";
//$_POST["Body"]="就是这样的!!!!!!!!!";
$mail=new PHPMailer();
$mail->CharSet="GB2312";
$mail->IsSMTP();
$mail->SMTPSecure = "ssl"; // sets the prefix to the servier
$mail->Host="smtp.gmail.com";           // SMTP服务器地址
$mail->Port = 465; // set the SMTP port for the GMAIL server
$mail->SMTPAuth=true;                           // SMTP是否需要验证，现在STMP服务器基本上都需要验证
$mail->Username="buptScale07";           // 登录用户名
$mail->Password="scalebupt";            //  登录密码
$mail->From="buptScale07@gmail.com";    //  发件人地址(username@163.com)
$mail->FromName="Eve";                    //   发件人名称
$mail->AddAddress($_POST["address"]);               //这里是收件人地址(test@hnce.net)
$mail->WordWrap=40;
//$mail->IsHTML(true);
$mail->Subject=$_POST["Subject"];
$mail->Body=$_POST["Body"];
$result=-14;
if(!$mail->Send())
	{
        $result=-14;
	} 
else
	{
		$result=1;
	}
buildXML($result);
?>