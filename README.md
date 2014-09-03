Kanban-Board 2012
============
A demo(spike) of a Kanban Board where you can move, edit and delete notes. 
The cool thing is that all changes you make are updated in real time to other connected clients via websockets.

At first I built this to test Play framework 1.2.4(this was 2012).

To implement websockets was really easy until I wanted to move the code to a cloud service, 
seems like there isn't a cloud service that supports websockets at the moment. 
Then I found out about Pusher(which is a great danish movie as well) and that was even easier and 
worked on Heroku cloud service! 

Should mention that the sandbox version of Pusher that I use only supports 20 connections.
