(ns channel
  (:use-macros [dommy.macros :only [sel sel1]])
  (:require [cljs.core.async :as async :refer [<! >! chan close! timeout]])
  (:require-macros [cljs.core.async.macros :as m :refer [go alt!]]))
(defn greet [n]
  (str "Hello you" n))

(.define WinJS.UI.Pages "/default.html" (clj->js {"ready" (fn [element, options] 
       (set! (.-innerText (sel1 :#timeout)) (greet "Clojurescript"))
       (let [acc (chan)
            accelerometer (.getDefault Windows.Devices.Sensors.Accelerometer)
            inkManager (Windows.UI.Input.Inking.InkManager.)
            inkCanvas (sel1 :#inkCanvas)
            inkContext (.getContext inkCanvas "2d")]
	        (set! accelerometer.-reportInterval 10)
            (.log js/%console inkContext)
            ; seems this gets lost if no event is recieved. or something
            (.addEventListener accelerometer "readingchanged" (fn [meter] 
                (go (>! acc (.-reading.accelerationX meter)))))           
            (test acc)))}))

(defn test [acc]
  (let [beb (chan)]
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
       (while true
			(<! beb)))))