<?php
require_once("../lib/PHPMailer/class.phpmailer.php");
require_once("../lib/PHPMailer/class.smtp.php");
include "../lib/ObjToXML.php";
//$_POST["address"]="everest1573@gmail.com";
//$_POST["Subject"]="����!";
//$_POST["Body"]="����������!!!!!!!!!";
$mail=new PHPMailer();
$mail->CharSet="GB2312";
$mail->IsSMTP();
$mail->SMTPSecure = "ssl"; // sets the prefix to the servier
$mail->Host="smtp.gmail.com";           // SMTP��������ַ
$mail->Port = 465; // set the SMTP port for the GMAIL server
$mail->SMTPAuth=true;                           // SMTP�Ƿ���Ҫ��֤������STMP�����������϶���Ҫ��֤
$mail->Username="buptScale07";           // ��¼�û���
$mail->Password="scalebupt";            //  ��¼����
$mail->From="buptScale07@gmail.com";    //  �����˵�ַ(username@163.com)
$mail->FromName="Eve";                    //   ����������
$mail->AddAddress($_POST["address"]);               //�������ռ��˵�ַ(test@hnce.net)
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