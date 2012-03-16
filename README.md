# clj-growlnotify

Clojure interface to the "growlnotify" command line tool to post growl notifications

## Usage

Hack to post growl messages thru command line "growlnotify" invocation.

UDP and java-based solutions required too much config or were unstable. This is probably more portable also... but I haven't tried anything but MacOSX.

Usage (use long-string "growlnotify" command line options):

		(use 'clj-growlnotify)
		(growl-notify "my title" "my message")
		(growl-notify "my title" "my message" :name "myapp" :sticky true)
		(growl-notify {:title "my title" :message "my message" :name "myapp" :sticky true})

Returns the title string.

## Prerequisites

You will have to install growl (http://growl.info/) and the growlnotify command line tool (http://growl.info/extras.php#growlnotify).

The resources folder has a growl-style file: BasicsCljDoc.growlStyle.
This file is based on Basics.growlStyle by Christopher Lobay (https://github.com/christopherlobay/basics.growlstyle) - very cool design and animation - thanks!!!
The only changes I made were setting the message-font to Inconsolata, which shows better for code-like info. Furthermore, I made the window wider to accomodate the standard doc-string width.

Double-clicking the BasicsCljDoc.growlStyle file will automatically install that style with Growl.

The resource folder also includes a copy of the Clojure logo as a gif-file, which can be used to associate the growl notification with clojure-related activity. Tom Hickey deserves credit for that beautiful design!!!

You should also install the Inconsolata font (http://www.google.com/webfonts/specimen/Inconsolata). Whether you use this growlnotify or not, you should install this font anyway for your favorite code editor as it's the best fixed-width "coding-font" out there (kudos for the designer, Raph Levien).

## License

Copyright (C) 2012 Frank Siebenlist

Distributed under the Eclipse Public License, the same as Clojure.

Growl style Basics by Christopher Lobay is licensed under the Creative Commons Attribution-Noncommercial-Share Alike 3.0 Unported License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
