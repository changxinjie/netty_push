Snip client connection instructions

	Frame format:
	header-2 bytes, device_token_id - 4 bytes, command -1 byte, data length -2 bytes, data -n bytes, checksum -1 byte
	-----------
	
	header: 0x00(plain text)/0x01(cipher text) (the snip client should set to 0x00), 0x00(data version, set 0 now).
	device_token_id: all set to 0 at snip client.
	data length: the data length n, should be converted to 2 bytes.
	data: JSON string format data.(two filed should be included I think, account_id_new,gateway_id), data for sensor alert may be {"account_id_new":419784,"gateway_id":784,"zone_id":1,"zone_type_id":3,"open":1}.
	checksum: To create it,can call the function CheckSumUtil.build(frame without checksum).
	command: 
		0x00: heat_beat
		0x01: Panel_status
		0x02: Zone_status
		0x03: Zwave_control_status
		0x04: Zwave_device_status
		0x05: Camera_status
		0x06: app_login

A.Using Netty framework. (server use 5.0Alpha2)

	There is a sample class named SnipClientBootstrap.
	
	The classes below should be noted.
		1.The class DeviceToken is reference to database table.	
		2.The class Message is the java been.
		3.There are also some usable util classes in the util package:
			ByteUtil: append byte array.
			CheckSumUtil: create the checksum and validate the data.
			JdbcUtil: a light weight ORM framework used in the server.
			ProertityUtil: load the properties configuration file.
			ThressDes: if the data is cipher text, the frame data is encoded by 3DES algorithm. This class is to encode/decode the data.
		4.There are two classes to convert between java been and byte array.
			NettyDecode is to decode data after receive data.
			NettyEncode is to encode before send data.
			
B.Using plain java socket or other implement.

	The server also supports the connection that created by plain java socket or any other socket implement.
	
	
	