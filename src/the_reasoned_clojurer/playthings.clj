(ns the-reasoned-clojurer.playthings
  (:refer-clojure :exclude [==])
  (:use [clojure.core.logic :only [run* s# u# == fresh conde run]]))


(run* [q]
      u#)
;;; => ()


(run* [q]
      (== true q))
;;; => (true)


(run* [q]
      u#
      (== true q))
;;; => ()


(run* [q]
      s#
      (== true q))
;;; => (true)


(run* [r]
      s#
      (== :corn r))
;;; => (:corn)


(run* [r]
      u#
      (= :corn r))
;;; => ()


(run* [q]
      s#
      (== false q))
;;; => (false)


(run* [x]
      (let [x false]
        (== true x)))
;;; => ()


(run* [q]
      (fresh [x]
             (== true x)
             (== true q)))
;;; => (true)


(run* [q]
      (fresh [x]
             (== x true)
             (== true q)))
;;; => (true)


(run* [q]
      (fresh [x]
             (== x true)
             (== q true)))
;;; => (true)


(run* [x]
      s#)
;;; => (_0)


(run* [x]
      (let [x false]
        (fresh [x]
               (== true x))))
;;; => (_0)


(run* [r]
      (fresh [x y]
             (== (conj () y x) r)))
;;; => ((_0 _1))


(run* [s]
      (fresh [t u]
             (== (conj () t u) s)))
;;; => ((_0 _1))


(run* [r]
      (fresh [x]
             (let [y x]
               (fresh [x]
                      (== (conj () y x y) r)))))
;;; => ((_0 _1 _0))


(run* [r]
      (fresh [x]
             (let [y x]
               (fresh [x]
                      (== (conj () x y x) r)))))
;;; => ((_0 _1 _0))


(run* [q]
      (== false q)
      (== true q))
;;; => ()


(run* [q]
      (== false q)
      (== false q))
;;; => (false)


(run* [q]
      (let [x q]
        (== true x)))
;;; => (true)


(run* [r]
      (fresh [x]
             (== x r)))
;;; => (_0)


(run* [q]
      (fresh [x]
             (== true x)
             (== x q)))
;;; => (true)


(run* [q]
      (fresh [x]
             (== x q)
             (== true x)))
;;; => (true)


(run* [x]
      (conde
       [(== :olive x) s#]
       [(== :oil x) s#]
       [s# u#]))
;;; => (:olive :oil)


(run 1 [x]
     (conde
      [(== :olive x) s#]
      [(== :oil x) s#]
      [s# u#]))
;;; => (:olive)


(run* [x]
      (conde
       [(== :virgin x) u#]
       [(== :olive x) s#]
       [s# s#]
       [(== :oil x) s#]
       [s# u#]))
;;; => (:olive _0 :oil)


(run 2 [x]
     (conde
      [(== :extra x) s#]
      [(== :virgin x) u#]
      [(== :olive x) s#]
      [(== :oil x) s#]
      [s# u#]))
;;; => (:extra :olive)


(run* [r]
      (fresh [x y]
             (== :split x)
             (== :pea y)
             (== (conj () y x) r)))
;;; => ((:split :pea))


(run* [r]
      (fresh [x y]
             (conde
              [(== :split x) (== :pea y)]
              [(== :navy x) (== :bean y)]
              [s# u#])
             (== (conj () y x) r)))
;;; => ((:split :pea) (:navy :bean))


(run* [r]
      (fresh [x y]
             (conde
              [(== :split x) (== :pea y)]
              [(== :navy x) (== :bean y)]
              [s# u#])
             (== (conj () :soup y x) r)))
;;; => ((:split :pea :soup) (:navy :bean :soup))


(defn teacupo
  [x]
  (conde
   [(== :tea x) s#]
   [(== :cup x) s#]
   [s# u#]))


(run* [x]
      (teacupo x))
;;; => (:tea :cup)


(run* [r]
      (fresh [x y]
             (conde
              [(teacupo x) (== true y) s#]
              [(== false x) (== true y)]
              [s# u#])
             (== (conj () y x) r)))
;;; => ((:tea true) (:cup true) (false true))


(run* [r]
      (fresh [x y z]
             (conde
              [(== y x) (fresh [x] (== z x))]
              [(fresh [x] (== y x)) (== z x)]
              [s# u#])
             (== (conj () z y) r)))
;;; => ((_0 _1) (_0 _1))


(run* [r]
      (fresh [x y z]
             (conde
              [(== y x) (fresh [x] (== z x))]
              [(fresh [x] (== y x)) (== z x)]
              [s# u#])
             (== false x)
             (== (conj () z y) r)))
;;; => ((false _0) (_0 false))


(run* [q]
      (let [a (== true q)
            b (== false q)]
        b))
;;; => (false)


(run* [q]
      (let [a (== true q)
            b (fresh [x]
                     (== x q)
                     (== false x))
            c (conde
               [(== true q) s#]
               [s# (== false q)])]
        b))
;;; => (false)
