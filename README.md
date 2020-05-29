# Firebase FCM

Mpesa-Checkout makes it easy to process online checkout using mpesa as the preferred method of payment.

## Getting Started

To get started follow this link to create an account with Firebase
[here](https://firebase.google.com/).

To make an API call, you will need:
* Firebase Server Key
* A User Access Token
 
Remember to completely connect your app to firebase.

### Installing

Basic Usage:

Step 1. **Add the JitPack repository to your build file**

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2. ** Add the dependency **

```
dependencies {
	   implementation 'com.github.Keeprawteach:FCM:1.0.0'
	}
```

Step 3. ** Initialize the SDK **

Create an instance of Message class:


```
    Message data = new Message();
    data.title = "Your desired title of the notification to send";
    data.token = "Pass your recipient FCM token";
    data.message = "Message to be sent in the notification";

    final FCMMessage message = new FCMMessage();
    message.setTo(token);
    message.setData(data);
```

Step 4. Call `sendMessage()` method to get an authorization token from Firebase API.

NOTE::

*ADD YOUR FIREBASE SERVER API* use ```FCM_KEY```

```
  FCMHelper.sendMessage(FCM_KEY, message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.e("REQUEST TO DRIVER", message.getData().toString());
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }
        });
```

## Handling The Message
REQUIREMENTS:
1. Ensure you have added firebase messaging dependency.
2. Follow the Complete steps provided [here](https://firebase.google.com/docs/cloud-messaging/android/client).


To handle incoming messages, in your `onMessageReceived`. add the following lines

```
  @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());

            sendNotification(getValue(remoteMessage.getData(), "title"),getValue(remoteMessage.getData(), "message"));


        }
    }
```
 Inside the `getValue()` method, check the sample
 
```
  public String getValue(Map<String, String> data, String key) {
         try {
             if (data.containsKey(key))
                 return data.get(key);
             else
                 return getString(R.string.app_name);
         } catch (Exception ex) {
             ex.printStackTrace();
             return getString(R.string.app_name);
         }
     }
```


## Built With

* [GSON](https://github.com/google/gson/) - A Java library that can be used to convert Java Objects into their JSON representation
* [RETROFIT](http://square.github.io/retrofit/) - Retrofit turns your HTTP API into a Java interface.
* [OKHTTP](http://square.github.io/okhttp/) - HTTP is the way modern applications network. It’s how we exchange data & media
* [RXJAVA](https://github.com/ReactiveX/RxJava/) - A library for composing asynchronous and event-based programs by using observable sequences.


## Authors

* **Kiprotich Japheth**

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Do NOT FORGET

Remember to enable internet permissions

