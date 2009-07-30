<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>PHP Mailer</title>
</head>
<body>
<h3>phpmailer Unit Test</h3>
请输入你的邮件详情:
<form name="phpmailer" action="php_mailer/php_mailer.php" method="post">
邮箱地址:
<br/>
<input type="text" size="50" name="address" />
<br/>
邮件标题:
<br/>
<input type="text" size="50" name="Subject" />
<br/>
邮件内容: 
<br/>
<input type="text" size="100" name="Body" />
<br/>
<br/>
<input type="submit" value="发送"/>
</form>
</body>
</html>
