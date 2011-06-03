These sources are the declarations of the types

The Application is built from different Units, that are language independent
declarations of the data structures. To use their content from program code,
the Language module generates source files - in Java6 this means interfaces
and enums inside, using the DustDeclarationConstants from dustAPI.
Of course, the content generation is not yet implemented, so these sources
are created and updated manually.