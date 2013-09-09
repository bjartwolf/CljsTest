(ns channel
  (:use-macros [dommy.macros :only [sel1]])
  (:require [dommy.core] [cljs.core.async :as async :refer [<! >! chan close! timeout]])
  (:require-macros [cljs.core.async.macros :as m :refer [go alt!]]))
(defn greet [n]
  (str "Hello you" n))


(defn test []
  (let [beb (chan)]
      (go
       (loop [x 1]
           (<! (timeout 200))
           (>! beb "hello")
		   (.log js/console x)
           (recur (+ x 1))))
      (go
       (loop [x 1]
			(.log js/console (<! beb))
			(recur (+ x 1))))))