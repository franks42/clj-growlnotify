;; Copyright (c) Frank Siebenlist. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file COPYING at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clj-growlnotify
  "Namespace with \"growl-notify\" clojure function to post growl messages.
  Usage: (use 'clj-growlnotity)
  which adds single \"growl-notify\" function to local ns"
  (:require [clojure.java.shell]
            [clojure.string]))


(defn growl-notify
	"Hack to post growl messages thru command line \"growlnotify\"
	 invocation. UDP and java-based solutions required too much config.
	 Usage (use long-string \"growlnotify\" command line options):
	 (growl-notify \"my title\" \"my message\")
	 (growl-notify \"my title\" \"my message\" :name \"myapp\" :sticky true)
	 (growl-notify {:title \"my title\" :message \"my message\" :sticky true})
	 Returns the clojure.java.shell/sh result of growlnotify invocation."
	([] (growl-notify {})) ; only default options - just for testing
	([opts-map] 
  	(let [default-map {	:title "Cljsh Growl Msg"
                        :sticky false
                        :name "cljsh"
                        :identifier "cljsh"
                        :priority 0
                        :host nil
                        :password nil
                        :appIcon "Clojure API"
                        :message "testing growl message from clojure"}
					growl-map (merge default-map opts-map)]
      (clojure.java.shell/sh "bash" "-c" 
        (clojure.string/join " " ["growlnotify"
             (when (:sticky growl-map) "--sticky")
             ;(when (:message growl-map) (str "--message \"" (:message growl-map) "\""))
             (when (:appIcon growl-map) (str "--appIcon \"" (:appIcon growl-map) "\""))
             (when (:identifier growl-map) 
               (str "--identifier \"" (:identifier growl-map) "\""))
             (when (:name growl-map) (str "--name \"" (:name growl-map) "\""))
              ;; following must come last according to man-page...
             (when (:title growl-map) (str "\"" (:title growl-map) "\""))
             ;; send message content thru stdin to avoid excessive quoting/escaping
             ]) :in (:message growl-map))))
  ([ttl msg & opts-kv]
	  (growl-notify (if opts-kv (apply assoc {:title ttl :message msg} opts-kv)
	                            {:title ttl :message msg}))))

