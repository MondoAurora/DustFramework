Core information structure

data: serialized form of actual data instances. Right here the first are the
core vendor (dust) and the types of the framework core. Of course, the format depends on the parser, so under data there may be more folders containing the different representations of the same data, together with some information about the parser.

source
 Java
  interface
   public	shared "data" interfaces for users
   private	private "logic" interfaces for implementor
  implementation
   [vendor]	local source tree

binary0
 JRE
  [vendor]	... jars
 linux		... .so files
 Windows	... .dll files
 native
  BeagleBoard	...
 
tools
 JRE
  database
   MySQL	... jars


Question: kernel will need generated files as well, where should they be? Perhaps under the kernel source tree, because they depent on the kernel AND the actual types included. They should never be committed back to the repository. (?)