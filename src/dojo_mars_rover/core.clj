(ns dojo-mars-rover.core
  (:require [clojure.java.io :as io])
  (:gen-class :main true))

(use '[clojure.string :only (split)])

(defn add-coordinates [c1 c2]
  {:x (+ (:x c1) (:x c2)) :y (+ (:y c1) (:y c2))})

(defn get-direction [orientation  command]
  (cond (and (= :north orientation) (= "f" command)) {:x 0 :y 1}
        (and (= :north orientation) (= "b" command)) {:x 0 :y -1}
        (and (= :north orientation) (= "l" command)) {:x -1 :y 0}
        (and (= :north orientation) (= "r" command)) {:x 1 :y 0}
        (and (= :south orientation) (= "f" command)) {:x 0 :y -1}
        (and (= :south orientation) (= "b" command)) {:x 0 :y 1}
        (and (= :south orientation) (= "l" command)) {:x 1 :y 0}
        (and (= :south orientation) (= "r" command)) {:x -1 :y 0}
        
        (and (= :east orientation) (= "f" command)) {:x 1 :y 0}
        (and (= :east orientation) (= "b" command)) {:x -1 :y 0}
        (and (= :east orientation) (= "l" command)) {:x 0 :y 1}
        (and (= :east orientation) (= "r" command)) {:x 0 :y -1}
        (and (= :west orientation) (= "f" command)) {:x -1 :y 0}
        (and (= :west orientation) (= "b" command)) {:x 1 :y 0}
        (and (= :west orientation) (= "l" command)) {:x 0 :y -1}
        (and (= :west orientation) (= "r" command)) {:x 0 :y 1}
        :default {:x 0 :y 0}))

(defn get-new-coordinates [position command]
  (let [direction (:orientation position)
        new-direction (get-direction direction command)
        coordinates {:x (:x position) :y (:y position)}
        new-coordinates (add-coordinates coordinates new-direction)]
    (if (or (= command "f")
            (= command "b"))
      (add-coordinates coordinates new-direction)
      coordinates)))

(defn turn-right [orientation]
  (cond (= :north orientation) :east
        (= :south orientation) :west
        (= :east orientation) :south
        (= :west orientation) :north))

(defn turn-left [orientation]
  (case orientation
    :north :west
    :south :east
    :east :north
    :west :south))


(defn calculate-new-orientation [command old-orientation]
  (cond (or  (= "f" command) (= "b" command)) old-orientation
        (= "r" command) (turn-right old-orientation)
        (= "l" command) (turn-left old-orientation)
        :default old-orientation)
  )

(defn calculate-new-position [old-position command]
  (let [old-orientation (:orientation old-position)
        new-orientation (calculate-new-orientation command old-orientation)]
    (merge (get-new-coordinates old-position command) 
           {:orientation new-orientation})))

(defn receive [init-position commands]
  (reduce calculate-new-position init-position (split  commands #"")))

(defn rover [x y orientation]
  {:x x :y y :orientation orientation})