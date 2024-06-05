<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>En.Ex.Se Verification Collaborator Email to Intranet</title>
    <link href="/css/template-style.css" rel="stylesheet"/>
    <style>html,body { padding: 0; margin:0; }</style>
</head>
<body>
<div style="font-family:Arial,Helvetica,sans-serif; line-height: 1.5; font-weight: normal; font-size: 15px; color: #2F3044; min-height: 100%; margin:0; padding:0; width:100%; background-color:#edf2f7">
    <table align="center" border="0" cellpadding="0" cellspacing="0" width="100%"
           style="border-collapse:collapse;margin:0 auto; padding:0; max-width:600px">
        <tbody>
        <tr>
            <td align="center" valign="center" style="text-align:center; padding: 40px">
                <a href="http://www.enexse.com/" rel="noopener" target="_blank">
                    <img src="https://dct-enexse.onrender.com/assets/images/EES-logoWeb-primary.png" alt="Logo Enexse"/>
                </a>
            </td>
        </tr>
        <tr>
            <td align="left" valign="center">
                <div style="text-align:left; margin: 0 20px; padding: 40px; background-color:#ffffff; border-radius: 6px">
                    <!--begin:Email content-->
                    <div style="padding-bottom: 30px; font-size: 17px;">
                        <strong>Bonjour ${name} (${userId}),</strong>
                    </div>
                    <div style="padding-bottom: 30px">To activate your account, please click on the button below to
                        verify your email address. Once activated, you’ll have full access to our Intranet Platform.
                    </div>
                    <div style="padding-bottom: 40px; text-align:center;">
                        <a href="${link}" rel="noopener"
                           style="text-decoration:none;display:inline-block;text-align:center;padding:0.75575rem 1.3rem;font-size:0.925rem;line-height:1.5;border-radius:0.35rem;color:#ffffff;background-color:#009EF7;border:0px;margin-right:0.75rem!important;font-weight:600!important;outline:none!important;vertical-align:middle"
                           target="_blank">Activate Account</a>
                    </div>
                    <div style="padding-bottom: 30px">This certification link will expire in <b>15 minutes.</b> After
                        expiring, if you did not request other confirmation link, no further action will be done.
                    </div>
                    <div style="border-bottom: 1px solid #eeeeee; margin: 15px 0"></div>
                    <div style="padding-bottom: 50px; word-wrap: break-all;">
                        <p style="margin-bottom: 10px;">Button not working? Try coping and pasting this URL into your
                            browser:</p>
                        <a href="${link}" rel="noopener" target="_blank" style="text-decoration:none;color: #009EF7">${link}</a>
                    </div>
                    <!--end:Email content-->
                    <div style="padding-bottom: 10px">Kind regards,
                        <br>The Enexse Software Management Team.
                            <tr>
                                <td align="center" valign="center"
                                    style="font-size: 13px; text-align:center;padding: 20px; color: #6d6e7c;">
                                    <p>11, Rue Marius Tercé – Parc “Aerotec” Bat. A, 31300, TOULOUSE.</p>
                                    <p>Copyright ©
                                        <a href="http://www.enexse.com/" rel="noopener" target="_blank">En.Ex.Se</a>.
                                    </p>
                                </td>
                            </tr>
                        </br>
                    </div>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
</body>
</html>