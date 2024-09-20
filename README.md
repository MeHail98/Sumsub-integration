# Sumsub-integration
1. Install ngrok (located in this folder) on your local computer and register (to replace localhost with the link that ngrok will give you)
You must register there just to get their security token,
it will then automatically go to some folder and everything will always work (you need a VPN)
Run ngrok: open the application and enter the command ngrok http 8080
In the Samsab dashboard, specify the path for the webhook as {ngrok link}/api/webhook - /api/webhook I just made this address in the code, so this

2. Install Postgres with drivers (download from the Postgres website, the database is local)

3. In the application.yml file, which is in the resources, specify all the necessary credentials in the sumsub section:
ap token, secret key, url for dev or prod, secret key for webhook
the application catches all webhooks, all will be saved to the local database

4. JDK version - Oracle open JDK 21.0.2 (File -> Project structure)

5. http://localhost:8080/api/start - starting point - enter in the search bar in the browser after starting the application. In IntelliJ idea, click the green arrow at the top of the screen and the program will start. Or you can run it through the ApplicationSumSub(src-main-java) file

The main Logic:

you can create applicants and immediately get a verification link from the applicant page or run the SDK
all applicants are created with null values ​​of the first name, last name and date of birth
the application catches the applicantReviewed webhook, makes a request to update the applicant's data, the applicant's data is updated on the side of the local database.

All webhooks are saved in the local database. Each webhook is checked for the validity of the secret key. If it is not valid, we will see in the application logs:
webhook not valid and the expected and received cipher value. If the webhook is not valid, the applicant will not be updated. This condition can be removed in WebhookController-> if
