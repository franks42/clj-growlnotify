;; Copyright (c) Frank Siebenlist. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file COPYING at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clj-growlnotify.gdoc
  "Functions to use growl notification to show clojure doc-info."
  (:require [clojure.java.shell]
            [clojure.string]
            [clj-growlnotify]
            [clj-info]
            [clj-info.doc2txt]))


(defn gdoc*
  "Function that presents documentation for a given identifier-string
  as a growl notification. Returns FQN of identifier."
  [w]
  (let [m   (clj-info.doc2txt/doc2txt w)
        ttl (:title m)
        msg (:message m)]
    (clj-growlnotify/growl-notify ttl msg :sticky true :name "clj-growlnotify.gdoc" :identifier "clj-info")
    ttl))

(defmacro gdoc
  "Macro that presents documentation of given identifier as
   a growl notification. Identifier can be given as string,
   symbol, or quoted symbol. Returns FQN of identifier.
   (gdoc map), (gdoc 'map) and (gdoc \"map\") are all equivalent."
  [w]
  (cond (string? w) `(gdoc* ~w)
        (symbol? w) `(gdoc* ~(str w))
        (= (type w) clojure.lang.Cons) `(gdoc* ~(str (second w)))))


;; register gdoc* with info facility, such that it can be used with (info...)
(clj-info/add-info-fn-map :growl gdoc*)

(defn -main [& args] )
