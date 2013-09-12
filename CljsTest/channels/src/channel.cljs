(ns channel
  (:use-macros [dommy.macros :only [sel sel1]])
  (:require [cljs.core.async :as async :refer [<! >! chan sliding-buffer close! timeout]])
  (:require-macros [cljs.core.async.macros :as m :refer [go alt!]]))
(defn greet [n]
  (str "Hello you" n)) 

(.define WinJS.UI.Pages "/default.html" (clj->js {"ready" (fn [element, options] 
       (set! (.-innerText (sel1 :#timeout)) (greet "Clojurescript"))
       (let [acc (chan (sliding-buffer 1))
            pointer (chan)
            accelerometer (.getDefault Windows.Devices.Sensors.Accelerometer)
            minimumReportInterval (.-minimumReportInterval accelerometer)
            inkManager (Windows.UI.Input.Inking.InkManager.)
            inkCanvas (sel1 :#inkCanvas)
            inkContext (.getContext inkCanvas "2d")]
	        (set! accelerometer.-ReportInterval minimumReportInterval)
(.addEventListener accelerometer "readingchanged" (fn [meter] 
                (go (>! acc (.toFixed (.-reading.accelerationX meter) 2)))))            
(.addEventListener inkCanvas "MSPointerDown" (fn [evt] 
                (go (>! pointer "PointerDown"))))     
            (.addEventListener inkCanvas "MSPointerUp" (fn [evt] 
                (go (>! pointer "PointerUp"))))  
            (.addEventListener inkCanvas "MSPointerMove" (fn [evt] 
                (go (>! pointer "PointerMove"))))  
            (go 
                (while true
                    (let [x (<! pointer)]
                       (set! (.-innerText (sel1 :#move)) x))))      
            (go 
                (while true
                    (let [x (<! acc)]
                    (set! (.-innerText (sel1 :#acceleration)) x))))
            (test)))}))

(defn test []
  (let [beb (chan)]
     (go
       (loop [x 1]
           (<! (timeout 1000))
           (>! beb "hello")
           (set! (.-innerText (sel1 :#timer)) x)
           (recur (+ x 1))))
     (go
       (while true
			(<! beb)))))