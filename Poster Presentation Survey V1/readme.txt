QR Code provides a token to the client(mobile app)
Client stores the token
It will now send the token to server for validation
If the token is valid then server will send userid,login success message
Now the client is logged in and can send other requests
Client uses the stored token and sends it along with on every request
Server verifies token and responds with data