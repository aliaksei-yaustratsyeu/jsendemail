1) Set JSENDEMAIL_HOME environment variable to the jsendemail home directory.

2) Go to the %JSENDEMAIL_HOME%\bin folder

3) Run the jsendemail program with needed arguments.

Command to run:

    jsendemail -from <email_address> -to <email_address> -s "<subject>" -m "<message>" [ -f <file with text> -a "<attachment>;<attachment>;..." -i "<inline>;<inline>;..."] 

Example:

    jsendemail -from alex@yourdomain.com -to "anna@gmail.com,aleksey@gmail.com,andrey@tut.by" -s "Hi!" -m "How are you doing?" -a "d:\Dropbox\*.jpg;d:\photos\*.jpg"