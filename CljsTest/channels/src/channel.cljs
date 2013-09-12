(ns channel
  (:use-macros [dommy.macros :only [sel sel1]])
  (:require [dommy.core] [dommy.attrs] [dommy.utils] [cljs.core.async :as async :refer [<! >! chan close! timeout]])
  (:require-macros [cljs.core.async.macros :as m :refer [go alt!]]))
(defn greet [n]
  (str "Hello you" n))

(.define WinJS.UI.Pages "/default.html" (clj->js {"ready" (fn [element, options] 
           (set! (.-innerText (sel1 :#timeout)) (greet "Clojurescript"))
           (test))}))

(defn test []
  (let [beb (chan)
        acc (chan)
        accelerometer (.getDefault Windows.Devices.Sensors.Accelerometer)]
	  (set! accelerometer.-reportInterval 100)
      (.addEventListener accelerometer "readingchanged" (fn [meter] 
           (go (>! acc (.-reading.accelerationX meter)))))
      (go 
        (while true
            (let [x (<! acc)]
                 (set! (.-innerText (sel1 :#acceleration)) x))))
     (go
       (loop [x 1]
           (<! (timeout 1000))
           (>! beb "hello")
           (set! (.-innerText (sel1 :#timer)) x)
           (recur (+ x 1))))
     (go
       (loop [x 1]
			(<! beb)
			(recur (+ x 1))))))