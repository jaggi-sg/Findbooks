# Findbooks
Android app to find books in bookstore using Android BLE technology.
Developed using TI sensor Tags (CC2541).


* The application makes use of Bluetooth Low Energy technology. 
(http://en.wikipedia.org/wiki/Bluetooth_low_energy)

* The key BLE terms and concepts can be found at https://developer.android.com/guide/topics/connectivity/bluetooth-le.html

* When the user launches the app, it scans for nearby bluetooth smart sensors (Like the CC5241 used in project) which are the categories of books in bookstore and lists them

* On selection of a category, the list of books for that category is returned along with the excat location of the book

* Requirements to run the app
  * Android version 4.3 and later (BLE Compatible)
  * Any sensor tags that supports Bluetooth smart
