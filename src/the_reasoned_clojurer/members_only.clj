(ns the-reasoned-clojurer.members-only
  (:refer-clojure :exclude [==])
  (:use [clojure.core.logic :only [run* == conde emptyo u# s# fresh resto run llist firsto conso]]
        [the-reasoned-clojurer.seeing-old-friends-in-new-ways :only [eq-car? eq-caro]]))


(defn mem
  [x l]
  (cond (empty? l)
        false

        (eq-car? l x)
        l

        :else
        (recur x (rest l))))


(run* [out]
      (== (mem :tofu [:a :b :tofu :d :peas :e]) out))
;;; => ((:tofu :d :peas :e))


(defn memo
  [x l out]
  (conde
   [(emptyo l) u#]
   [(eq-caro l x) (== l out)]
   [s# (fresh [d]
              (resto l d)
              (memo x d out))]))


(run 1 [out]
     (memo :tofu [:a :b :tofu :d :tofu :e] out))
;;; => ((:tofu :d :tofu :e))


(run 1 [out]
     (fresh [x]
            (memo :tofu [:a :b x :d :tofu :e] out)))
;;; => ((:tofu :d :tofu :e))


(run* [r]
      (memo r [:a :b :tofu :d :tofu :e] [:tofu :d :tofu :e]))
;;; => (:tofu)


(run* [q]
      (memo :tofu [:tofu :e] [:tofu :e])
      (== true q))
;;; => (true)


(run* [q]
      (memo :tofu [:tofu :e] [:tofu])
      (== true q))
;;; => ()


(run* [x]
      (memo :tofu [:tofu :e] [x :e]))
;;; => (:tofu)


(run* [x]
      (memo :tofu [:tofu :e] [:peas x]))
;;; => ()


(run* [out]
      (fresh [x]
             (memo :tofu [:a :b x :d :tofu :e] out)))
;;; => ((:tofu :d :tofu :e)
;;;     (:tofu :e))


(run 12 [z]
     (fresh [u]
            (memo :tofu (llist :a :b :tofu :d :tofu :e z) u)))
;;; => (_0
;;;     _0
;;;     (:tofu . _0)
;;;     (_0 :tofu . _1)
;;;     (_0 _1 :tofu . _2)
;;;     (_0 _1 _2 :tofu . _3)
;;;     (_0 _1 _2 _3 :tofu . _4)
;;;     (_0 _1 _2 _3 _4 :tofu . _5)
;;;     (_0 _1 _2 _3 _4 _5 :tofu . _6)
;;;     (_0 _1 _2 _3 _4 _5 _6 :tofu . _7)
;;;     (_0 _1 _2 _3 _4 _5 _6 _7 :tofu . _8)
;;;     (_0 _1 _2 _3 _4 _5 _6 _7 _8 :tofu . _9)


(defn memo
  [x l out]
  (conde
   [(eq-caro l x) (== l out)]
   [s# (fresh [d]
              (resto l d)
              (memo x d out))]))


(defn rember
  [x l]
  (cond (empty? l)
        ()

        (eq-car? l x)
        (rest l)

        :else
        (conj (rember x (rest l)) (first l))))


(defn rembero
  [x l out]
  (conde
   [(emptyo l) (== () out)]
   [(eq-caro l x) (resto l out)]
   [s# (fresh [res]
              (fresh [d]
                     (resto l d)
                     (rembero x d res))
              (fresh [a]
                     (firsto l a)
                     (conso a res out)))]))


(defn rembero
  [x l out]
  (conde
   [(emptyo l) (== () out)]
   [(eq-caro l x) (resto l out)]
   [s# (fresh [a d res]
              (conso a d l)
              (rembero x d res)
              (conso a res out))]))


(run 1 [out]
     (fresh [y]
            (rembero :peas [:a :b y :d :peas :e] out)))
;;; => ((:a :b :d :peas :e))


(run* [out]
      (fresh [y z]
             (rembero y [:a :b y :d z :e] out)))
;;; => ((:b :a :d _0 :e)
;;;     (:a :b :d _0 :e)
;;;     (:a :b :d _0 :e)
;;;     (:a :b :d _0 :e)
;;;     (:a :b _0 :d :e)
;;;     (:a :b :e :d _0)
;;;     (:a :b _0 :d _1 :e))


(run* [r]
      (fresh [y z]
             (rembero y [y :d z :e] [y :d :e])
             (== [y z] r)))
;;; => ([:d :d]
;;;     [:d :d]
;;;     [_0 _0]
;;;     [:e :e])


(run 13 [w]
     (fresh [y z out]
            (rembero y (llist :a :b y :d z w) out)))
;;; => (_0
;;;     _0
;;;     _0
;;;     _0
;;;     _0
;;;     ()
;;;     (_0 . _1)
;;;     (_0)
;;;     (_0 _1 . _2)
;;;     (_0 _1)
;;;     (_0 _1 _2 . _3)
;;;     (_0 _1 _2)
;;;     (_0 _1 _2 _3 . _4))


(defn surpriseo
  [s]
  (rembero s [:a :b :c] [:a :b :c]))


(run* [r]
      (== :d r)
      (surpriseo r))
;;; => (:d)


(run* [r]
      (surpriseo r))
;;; => (_0)


(run* [r]
      (surpriseo r)
      (== r :b))
;;; => (:b)
