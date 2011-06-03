These boot sources are required for Modular builds.

Modular builds start up without a kernel, but the startup routines require some API
functions to create the deployment information objects, and perhaps extend them with
actual startup parameters. 
This information can be used when loading the kernel and then is passed to it to be 
used when really starting up the application.

For Monolithic builds this is not required, because then all the Modules including 
the kernel is build together with the startup code, so the first API is the real one.  