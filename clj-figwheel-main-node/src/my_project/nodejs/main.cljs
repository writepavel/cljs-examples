(ns ^:figwheel-hooks my-project.nodejs.main
  (:require [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

(def hello-message "Hello world!!!")

(defn -main []
    (println "hello 2"))

(set! *main-cli-fn* -main) ;; this is required

(defn ^:before-load my-before-reload-callback []
  (println "BEFORE reload 4!!!"))

(defn ^:after-load my-after-reload-callback []
  (println "AFTER reload!!!"))

(println "STARTED!2")
