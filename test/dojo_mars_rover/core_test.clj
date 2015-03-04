(ns dojo-mars-rover.core-test
  (:require [clojure.test :refer :all]
            [dojo-mars-rover.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(deftest test-position
  (testing "right direction?"
    (is (= {:x 0 :y -1} (get-direction :north "f")))))


