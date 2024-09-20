# Sumsub-integration
1. Install ngrok (located in this folder) on your local computer and register (to replace localhost with the link that ngrok will give you)
You must register there just to get their security token,
it will then automatically go to some folder and everything will always work (you need a VPN)
Run ngrok: open the application and enter the command ngrok http 8080
In the Samsab dashboard, specify the path for the webhook as {ngrok link}/api/webhook - /api/webhook I just made this address in the code, so this

2. Install Postgres with drivers (download from the Postgres website or I have it in my folder, just run the installer, the database is local)

3. Unzip the archive. There is a folder in a folder, so you can delete one folder, just leave the SpringFinal folder and all the files in it. In IntelliJ idea, open the project: open->SpringFinal

4. In the application.yml file, which is in the resources, specify all the necessary credentials in the sumsub section:
ap token, secret key, url for dev or prod, secret key for webhook
the application catches all webhooks, all will be saved to the local database

4. JDK version - Oracle open JDK 21.0.2 (File -> Project structure)

5. http://localhost:8080/api/start - starting point - enter in the search bar in the browser after starting the application. In IntelliJ idea, click the green arrow at the top of the screen and the program will start. Or you can run it through the ApplicationSumSub(src-main-java) file

it may be difficult to figure out, but in fact everything is quite flexible there, but the main Logic:

you can create applicants and immediately get a verification link from the applicant page or run the SDK
all applicants are created with null values ​​of the first name, last name and date of birth
the application catches the applicantReviewed webhook, makes a request to update the applicant's data, the applicant's data is updated on the side of the local database.

With other webhooks - I don't know the behavior, I haven't tested it, I sharpened everything for the applicant review, in the ApplicantService file in the update method
you can simply add a line to check that the webhook is exactly applicant review and update the applicant only if it is exactly this webhook, but I haven't done this check

All webhooks are saved in the local database. Each webhook is checked for the validity of the cert key. If it is not valid, we will see in the application logs:
webhook not valid and the expected and received cipher value. If the webhook is not valid, the applicant will not be updated. This condition can be removed in WebhookController-> if
