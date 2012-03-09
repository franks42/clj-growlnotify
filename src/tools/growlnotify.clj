;; Copyright (c) Frank Siebenlist. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file COPYING at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns tools.growlnotify
    (:require [clojure.java.shell]
              [clojure.string]
              [cljsh.docs]))

(defn growl-notify
	"Hack to post growl messages thru command line \"growlnotify\" invocation.
	 UDP and java-based solutions required too much config or were unstable.
	 Usage (use long-string \"growlnotify\" command line options):
	 (growl-notify \"my title\" \"my message\")
	 (growl-notify \"my title\" \"my message\" :name \"myapp\" :sticky true)
	 (growl-notify {:title \"my title\" :message \"my message\" :name \"myapp\" :sticky true})
	 Returns the clojure.java.shell/sh result of growlnotify invocation."
	([] (growl-notify {})) ; only default options - just for testing
	([ttl msg & opts]
	  (growl-notify (if opts  (apply assoc {:title ttl :message msg} opts)
	                            {:title ttl :message msg})))
	([gmap] 
  	(let [default-map {	:title "Cljsh Growl Msg"
                        :sticky false
                        :name "cljsh"
                        :identifier "cljsh"
                        :priority 0
                        :host nil
                        :password nil
                        :appIcon "Clojure API"
                        :message "testing growl message from clojure"}
						growl-map (merge default-map gmap)]
				(clojure.java.shell/sh "bash" "-c" 
					(clojure.string/join " " ["growlnotify"
							 (when (:sticky growl-map) "--sticky")
							 ;; send message content thru stdin to avoid excessive quoting/escaping
							 ;(when (:message growl-map) (str "--message \"" (:message growl-map) "\""))
							 (when (:appIcon growl-map) (str "--appIcon \"" (:appIcon growl-map) "\""))
							 (when (:identifier growl-map) (str "--identifier \"" (:identifier growl-map) "\""))
							 (when (:name growl-map) (str "--name \"" (:name growl-map) "\""))
                ;; following must come last according to man-page...
							 (when (:title growl-map) (str "\"" (:title growl-map) "\""))
							 ]) :in (:message growl-map)))))
							 
(defn gdoc*
  "Function that presents documentation for a given identifier-string
  as a growl notification. Returns FQN of identifier."
  [w]
  (let [m (cljsh.docs/docs-map w)
        tlt (if m 
              (str  (or (:fqname m)(:name m))
                    "    -    " 
                    (when (:private m) "Private ")
                    (cond (:macro m) "Macro"
                          (:special-form m) "Special Form"
                          (:namespace m) "Namespace"
                          (:protocol-def m) "Protocol"
                          (:protocol-member-fn m) "Member Interface/Function"
                          (:function m) "Function"
                          (:java-class m) "java.lang.Class"))
              (str "Sorry, no doc-info for \\\"" w "\\\""))
        msg (if m
              (str  (when (:forms m) (doall (apply str (map pr-str (:forms m)))))
                    (when (:arglists m) (doall (apply str (map pr-str (:arglists m)))))
                    \newline
                    (:doc m))
              "")]
    (growl-notify tlt msg :sticky true :name "tools.growlnotify/gdoc" :identifier "clj-doc")
    tlt))

(defmacro gdoc
  "Macro that presents documentation of given identifier as 
   a growl notification. Identifier can be given as string, 
   symbol, or quoted symbol. Returns FQN of identifier.
   (gdoc map), (gdoc 'map) and (gdoc \"map\") are all equivalent."
  [w]
  (cond (string? w) (gdoc* w) 
        (symbol? w) (gdoc* (str w))
        (= (type w) clojure.lang.Cons) (gdoc* (str (second w)))))
