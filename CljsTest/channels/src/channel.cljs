(ns channel
  (:use-macros [dommy.macros :only [sel sel1]])
  (:require [dommy.core] [dommy.attrs] [dommy.utils] [cljs.core.async :as async :refer [<! >! chan close! timeout]])
  (:require-macros [cljs.core.async.macros :as m :refer [go alt!]]))
(defn greet [n]
  (str "Hello you" n))

(.define WinJS.UI.Pages "/default.html" (clj->js {ready (fn [element, options] 
           (set! (.-innerText (sel1 :#timeout)) (greet "Clojurescript"))
           (test))}))

(defn test []
  (let [beb (chan)]
      (go
       (loop [x 1]
           (<! (timeout 200))
           (>! beb "hello")
		   (.log js/console x)
           (set! (.-innerText (sel1 :#acceleration)) x)
           (.log js/console (sel1 :#acceleration))
           (recur (+ x 1))))
      (go
       (loop [x 1]
			(.log js/console (<! beb))
			(recur (+ x 1))))))