(ns the-reasoned-clojurer.seeing-old-friends-in-new-ways
  (:refer-clojure :exclude [== identity])
  (:use [clojure.core.logic :only [conde emptyo s# resto fresh u# run* run lcons
                                   llist firsto == conso]]
        [the-reasoned-clojurer.teaching-old-toys-new-tricks :only [pairo]]))


;;; clojure's `list?` function works fine on `llist`s


(defn listo
  [l]
  (conde
   [(emptyo l) s#]
   [(pairo l) (fresh [d]
                     (resto l d)
                     (listo d))]
   [s# u#]))


(run* [x]
      (listo [:a :b x :d]))
;;; => (_0)


(run 1 [x]
     (listo (llist :a :b :c x)))
;;; => (())


;; (run* [x]
;;       (listo (llist :a :b :c x)))
;;; => ...


(run 5 [x]
     (listo (llist :a :b :c x)))
;;; => (() (_0) (_0 _1) (_0 _1 _2) (_0 _1 _2 _3))


(defn lol?
  [l]
  (cond (empty? l)
        true

        (list? (first l))
        (recur (rest l))

        :else
        false))


(defn lolo
  [l]
  (conde
   [(emptyo l) s#]
   [(fresh [a]
           (firsto l a)
           (listo a))
    (fresh [d]
           (resto l d)
           (lolo d))]
   [s# u#]))


(run 1 [l]
     (lolo l))
;;; => (())


(run* [q]
      (fresh [x y]
             (lolo [[:a :b] [x :c] [:d y]])
             (== true q)))
;;; => (true)


(run 1 [x]
     (lolo (llist [:a :b] [:c :d] x)))
;;; => (())


(run 5 [x]
     (lolo (list [:a :b] [:c :d] x)))
;;; => (() (_0) (_0 _1) (_0 _1 _2) (_0 _1 _2 _3))


(defn twinso
  [s]
  (fresh [x y]
         (conso x () y)
         (conso x y s)))


(run* [q]
      (twinso [:tofu :tofu])
      (== true q))
;;; => (true)


(run* [z]
      (twinso [z :tofu]))
;;; => (:tofu)


(defn twinso
  [s]
  (fresh [x]
         (== (llist x x ()) s)))


(defn loto
  [l]
  (conde
   [(emptyo l) s#]
   [(fresh [a]
           (firsto l a)
           (twinso a))
    (fresh [d]
           (resto l d)
           (loto d))]
   [s# u#]))


(run 1 [z]
     (loto (llist [:g :g] z)))
;;; => (())


(run 5 [z]
     (loto (llist [:g :g] z)))
;;; => (() ((_0 _0)) ((_0 _0) (_1 _1)) ((_0 _0) (_1 _1) (_2 _2)) ((_0 _0) (_1 _1) (_2 _2) (_3 _3)))


(run 5 [r]
     (fresh [w x y z]
            (loto (llist [:g :g] [:e w] [x y] z))
            (== [w [x y] z] r)))
;;; => ([:e [_0 _0] ()] [:e [_0 _0] ((_1 _1))] [:e [_0 _0] ((_1 _1) (_2 _2))]
;;;     [:e [_0 _0] ((_1 _1) (_2 _2) (_3 _3))] [:e [_0 _0] ((_1 _1) (_2 _2) (_3 _3) (_4 _4))])


(run 3 [out]
     (fresh [w x y z]
            (== (llist [:g :g] [:e w] [x y] z) out)
            (loto out)))
;;; => (([:g :g] [:e :e] [_0 _0] ()) ([:g :g] [:e :e] [_0 _0] ((_1 _1)))
;;;     ([:g :g] [:e :e] [_0 _0] ((_1 _1) (_2 _2))))


(defn listofo
  [predo l]
  (conde
   [(emptyo l) s#]
   [(fresh [a]
           (firsto l a)
           (predo a))
    (fresh [d]
           (resto l d)
           (listofo predo d))]
   [s# u#]))


(run 3 [out]
     (fresh [w x y z]
            (== (llist [:g :g] [:e w] [x y] z) out)
            (listofo twinso out)))
;;; => (([:g :g] [:e :e] [_0 _0])
;;;     ([:g :g] [:e :e] [_0 _0] (_1 _1))
;;;     ([:g :g] [:e :e] [_0 _0] (_1 _1) (_2 _2)))


(defn loto
  [l]
  (listofo twinso l))


(defn eq-car?
  [l x]
  (= (first l) x))


(defn member?
  [x l]
  (cond (empty? l)
        false

        (eq-car? l x)
        true

        :else
        (recur x (rest l))))


(defn eq-caro
  [l x]
  (firsto l x))


(defn membero
  [x l]
  (conde
   [(emptyo l) u#]
   [(eq-caro l x) s#]
   [(fresh [d]
           (resto l d)
           (membero x d))]))


(run* [q]
      (membero :olive [:virgin :olive :oil])
      (== true q))
;;; => (true)


(run 1 [y]
     (membero y [:hummus :with :pita]))
;;; => (:hummus)


(run 1 [y]
     (membero y [:with :pita]))
;;; => (:with)


(run 1 [y]
     (membero y [:pita]))
;;; => (:pita)


(run 1 [y]
     (membero y ()))
;;; => ()


(run* [y]
      (membero y [:hummus :with :pita]))
;;; => (:hummus :with :pita)


(defn identity
  [l]
  (run* [y]
        (membero y l)))


(run* [x]
      (membero :e [:pasta x :faioli]))
;;; => (:e)


(run 1 [x]
     (membero :e [:pasta :e x :fagioli]))
;;; => (_0)


(run 1 [x]
     (membero :e [:pasta x :e :fagioli]))
;;; => (:e)


(run* [r]
      (fresh [x y]
             (membero :e [:pasta x :fagioli y])
             (== [x y] r)))
;;; => ([:e _0] [_0 :e])


(run 1 [l]
     (membero :tofu l))
;;; ((:tofu . _0))


(run 5 [l]
     (membero :tofu l))
;;; ((:tofu . _0)
;;;  (_0 :tofu . _ 1)
;;;  (_0 _1 :tofu . _2)
;;;  (_0 _1 _2 :tofu . _3)
;;;  (_0 _1 _2 _3 :tofu . _4))


(defn pmembero
  [x l]
  (conde
   [(emptyo l) u#]
   [(eq-caro l x) (resto l ()) s#]
   [(fresh [d] (resto l d) (pmembero x d))]))


(run 5 [l]
     (pmembero :tofu l))
;;; ((:tofu)
;;;  (_0 :tofu)
;;;  (_0 _1 :tofu)
;;;  (_0 _1 _2 :tofu)
;;;  (_0 _1 _2 _3 :tofu))


(run* [q]
      (pmembero :tofu [:a :b :tofu :d :tofu])
      (== true q))
;;; (true)


(defn pmembero
  [x l]
  (conde
   [(emptyo l) u#]
   [(eq-caro l x) (resto l ())]
   [(eq-caro l x) s#]
   [(fresh [d]
           (resto l d)
           (pmembero x d))]))


(run* [q]
      (pmembero :tofu [:a :b :tofu :d :tofu])
      (== true q))
;;; => (true true true)


(defn pmembero
  [x l]
  (conde
   [(eq-caro l x) (resto l ())]
   [(eq-caro l x) (fresh [a d]
                         (resto l (lcons a d)))]
   [s# (fresh [d]
              (resto l d)
              (pmembero x d))]))


(run* [q]
      (pmembero :tofu [:a :b :tofu :d :tofu])
      (== true q))
;;; => (true true)


(run 12 [l]
     (pmembero :tofu l))
;;; => ((:tofu)
;;;     (:tofu _0 . _1)
;;;     (_0 :tofu)
;;;     (_0 :tofu _1 . _2)
;;;     (_0 _1 :tofu)
;;;     (_0 _1 :tofu _2 . _3)
;;;     (_0 _1 _2 :tofu)
;;;     (_0 _1 _2 :tofu _3 . _4)
;;;     (_0 _1 _2 _3 :tofu)
;;;     (_0 _1 _2 _3 :tofu _4 . _5)
;;;     (_0 _1 _2 _3 _4 :tofu)
;;;     (_0 _1 _2 _3 _4 :tofu _5 . _6))


(defn pmembero
  [x l]
  (conde
   [(eq-caro l x) (fresh [a d]
                         (resto l (lcons a d)))]
   [(eq-caro l x) (resto l ())]
   [s# (fresh [d]
              (resto l d)
              (pmembero x d))]))


(run 12 [l]
     (pmembero :tofu l))
;;;  (:tofu _0 . _1)
;;;  (:tofu)
;;;  (_0 :tofu _1 . _2)
;;;  (_0 :tofu)
;;;  (_0 _1 :tofu _2 . _3)
;;;  (_0 _1 :tofu)
;;;  (_0 _1 _2 :tofu _3 . _4)
;;;  (_0 _1 _2 :tofu)
;;;  (_0 _1 _2 _3 :tofu _4 . _5)
;;;  (_0 _1 _2 _3 :tofu)
;;;  (_0 _1 _2 _3 _4 :tofu _5 . _6)
;;;  (_0 _1 _2 _3 _4 :tofu)


(defn first-value
  [l]
  (run 1 [y]
       (membero y l)))


(first-value [:pasta :e :fagioli])
;;; => (:pasta)


(defn memberrevo
  [x l]
  (conde
   [s# (fresh [d]
              (resto l d)
              (memberrevo x d))]
   [s# (eq-caro l x)]))


;;; surprisingly, doesn't reverse the list
(run* [x]
      (memberrevo x [:pasta :e :fagioli]))
;;; => (:pasta :e :fagioli)


(defn reverse-list
  [l]
  (run* [y]
        (memberrevo y l)))
