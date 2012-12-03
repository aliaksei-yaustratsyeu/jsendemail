
JSendEmail util helps to send emails with attachments from command line. So you can use it in your scripts. 

Example:

    jsendemail -from alex@yourdomain.com -to "anna@gmail.com,aleksey@gmail.com,andrey@tut.by" -s "Hi!" -m "How are you doing?" -a "d:\Dropbox\*.jpg;d:\photos\*.jpg"

How to use it:

1) Set JSENDEMAIL_HOME environment variable to the jsendemail home directory.

2) Go to the %JSENDEMAIL_HOME%\conf folder and configure your smtp credentials.

    If you use Amazon SES you should set accessKey and secretKey properties in AwsCredentials.properties configuration file 
    or you can set username and password for Amazon SES smtp server in settings.properties file. If you want use accessKey and secretKey 
    you should check that username and password values in settings.properties file are empty. 
    
    Example of configuration for Amazon SES if you hava smtp username and password values:
    
        mail.host=email-smtp.us-east-1.amazonaws.com
        mail.port=465
        mail.protocol=smtps
        # you can miss username and password if you use AWS Credentials - accessKey and secretKey  (in AwsCredentials.properties file)
        # otherwise you should set it to the smtp server username and password values
        mail.username=some_username
        mail.password=some_password
        mail.auth=true
        mail.starttls.enable=true
        mail.debug=true
    
    If you use Google smtp server you should comment Amazion SES configuration and uncomment the next lines:
    
        mail.host=smtp.gmail.com
        mail.port=465
        mail.protocol=smtps
        mail.username=username@gmail.com
        mail.password=password
        mail.auth=true
        mail.starttls.enable=true
        mail.debug=true
    
    Set username and password on your own values.
    
    If you want to use other smtp server you should change values for mail.host, mail.port, mail.username, mail.password properties.

3) Go to the %JSENDEMAIL_HOME%\bin folder

4) Run the jsendemail program with needed arguments.

Command to run:

    jsendemail -from <email_address> -to <email_address> -s "<subject>" -m "<message>" [ -f <file with text> -a "<attachment>;<attachment>;..." -i "<inline>;<inline>;..."] 

Example:

    jsendemail -from alex@yourdomain.com -to "anna@gmail.com,aleksey@gmail.com,andrey@tut.by" -s "Hi!" -m "How are you doing?" -a "d:\Dropbox\*.jpg;d:\photos\*.jpg"