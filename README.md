# FakePokemonLocation
Dummy udp server for android that mock gps location of a device.

You start the application and then you are able to send an arbitrary location by somthing like

echo -n '12.1234,-2.1234' | nc -4u 192.168.43.1 12345

where 
12.1234,-2.1234 is a desired location
192.168.43.1 is your device's ip
12345 is port for connection.
