Email necessities to be triggered from Azure:

1. whitelist outbound port in Azure (465 for gmail.smtp)
2. whitelist port in VM firewall
    > sudo firewall-cmd --permanent --add-port=465/tcp
    > sudo firewall-cmd --reload
    > sudo firewall-cmd --list-all
3. add the following params to AEM start:
    "-Dmail.smtp.starttls.enable=true -Dmail.smtp.ssl.protocols=TLSv1.2"
4. enable less secure apps and disalbe captcha
    - You must have to log intothe gmail account (the one used by the project).
    - Then go to https://www.google.com/settings/security/lesssecureapps and Turn On this feature.
    - And last go to https://accounts.google.com/DisplayUnlockCaptcha and click Continue.