(ns ^:figwheel-hooks my-project.nodejs.main
  (:require [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

(def hello-message "Hello world!!!")

(defn -main []
    (println "hello"))

(set! *main-cli-fn* -main) ;; this is required

(defn ^:before-load my-before-reload-callback []
  (println "BEFORE reload!!!"))

(defn ^:after-load my-after-reload-callback []
  (println "AFTER reload!!!"))
