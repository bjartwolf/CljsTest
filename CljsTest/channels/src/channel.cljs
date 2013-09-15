(ns channel
  (:use-macros [dommy.macros :only [sel sel1]])
  (:require [cljs.core.async :as async :refer [<! >! put! chan dropping-buffer sliding-buffer close! timeout]]
   [dommy.core :as dommy])
  (:require-macros [cljs.core.async.macros :as m :refer [go alt!]]))
(defn greet [n]
  (str "Hello Tuva " n)) 

(defn run-timer []
     (go
       (loop [x 1]
           (<! (timeout 1000))
           (set! (.-innerText (sel1 :#timer)) x)
           (recur (+ x 1)))))

(def acc (chan (dropping-buffer 10)))

(let [accelerometer (.getDefault Windows.Devices.Sensors.Accelerometer)
           minimumReportInterval (.-minimumReportInterval accelerometer)]
	        (set! accelerometer.-ReportInterval minimumReportInterval) 
            (.addEventListener accelerometer "readingchanged" (fn [meter] 
                    (put! acc (.toFixed (.-reading.accelerationX meter) 2)))))


(def pointer (chan (sliding-buffer 1))) 
(def xs (chan (sliding-buffer 1)))
 
(.define WinJS.UI.Pages "/default.html" (clj->js {"ready" (fn [element, options] 
       (set! (.-innerText (sel1 :#timeout)) (greet "sier hei"))
       (let [inkManager (Windows.UI.Input.Inking.InkManager.)
             inkCanvas (sel1 :#inkCanvas)
             inkContext (.getContext inkCanvas "2d")]
             (.addEventListener inkCanvas "MSPointerDown" (fn [evt] 
                (put! pointer "PointerDown")))     
            (.addEventListener inkCanvas "MSPointerUp" (fn [evt] 
                (put! pointer "PointerUp")))  
            (.addEventListener inkCanvas "MSPointerMove" (fn [evt] 
                (put! pointer "PointerMove")
                (put! xs (.-currentPoint.rawPosition.x evt))))  
            (go (while true
                    (let [x (<! xs)]
                     (set! (.-innerText (sel1 :#xs)) x))))
            (go (while true
                    (let [x (* 100 (<! acc))
                         T (sel1 :#T)
                         bredde (.-width T)]
                        ;save
                        (dommy/set-style! T :width (str (- bredde x)"px"))
                        (set! (.-innerText (sel1 :#acceleration)) x))))            
            (go (while true
                    (let [x (<! pointer)]
                       (set! (.-innerText (sel1 :#move)) x))))      
            (run-timer)))}))