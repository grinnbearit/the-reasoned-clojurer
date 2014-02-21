(ns the-reasoned-clojurer.double-your-fun
  (:refer-clojure :exclude [== flatten])
  (:use [clojure.core.logic :only [conde emptyo == s# fresh firsto resto conso run* llist run]]
        [the-reasoned-clojurer.teaching-old-toys-new-tricks :only [pairo]]))


(defn append
  [l s]
  (if (empty? l)
    s
    (conj (append (rest l) s) (first l))))


(defn appendo
  [l s out]
  (conde
   [(emptyo l) (== s out)]
   [s# (fresh [a d res]
              (firsto l a)
              (resto l d)
              (appendo d s res)
              (conso a res out))]))


(run* [x]
      (appendo [:cake] [:tastes :yummy] x))
;;; => ((:cake :tastes :yummy))


(run* [x]
      (fresh [y]
             (appendo [:cake :with :ice y] [:tastes :yummy] x)))
;;; => ((:cake :with :ice _0 :tastes :yummy))


(run* [x]
      (fresh [y]
             (appendo [:cake :with :ice :cream] y x)))
;;; => ((:cake :with :ice :cream . _0))


(run 1 [x]
     (fresh [y]
            (appendo (llist :cake :with :ice y) [:d :t] x)))
;;; => ((:cake :with :ice :d :t))


(defn appendo
  [l s out]
  (conde
   [(emptyo l) (== s out)]
   [s# (fresh [a d res]
              (conso a d l)
              (appendo d s res)
              (conso a res out))]))


(run 5 [x]
     (fresh [y]
            (appendo (llist :cake :with :ice y) [:d :t] x)))
;;; => ((:cake :with :ice :d :t)
;;;     (:cake :with :ice _0 :d :t)
;;;     (:cake :with :ice _0 _1 :d :t)
;;;     (:cake :with :ice _0 _1 _2 :d :t)
;;;     (:cake :with :ice _0 _1 _2 _3 :d :t))


(run 5 [y]
     (fresh [x]
            (appendo (llist :cake :with :ice y) [:d :t] x)))
;;; => (()
;;;     (_0)
;;;     (_0 _1)
;;;     (_0 _1 _2)
;;;     (_0 _1 _2 _3))


(run 5 [x]
     (fresh [y]
            (appendo (llist :cake :with :ice y) (llist :d :t y) x)))
;;; => ((:cake :with :ice :d :t)
;;;     (:cake :with :ice _0 :d :t _0)
;;;     (:cake :with :ice _0 _1 :d :t _0 _1)
;;;     (:cake :with :ice _0 _1 _2 :d :t _0 _1 _2)
;;;     (:cake :with :ice _0 _1 _2 _3 :d :t _0 _1 _2 _3))


(run* [x]
      (fresh [z]
             (appendo [:cake :with :ice :cream] (llist :d :t z) x)))
;;; => ((:cake :with :ice :cream :d :t _0))


(run 6 [x]
     (fresh [y]
            (appendo x y [:cake :with :ice :d :t])))
;;; => (()
;;;     (:cake)
;;;     (:cake :with)
;;;     (:cake :with :ice)
;;;     (:cake :with :ice :d)
;;;     (:cake :with :ice :d :t))


(run 6 [y]
     (fresh [x]
            (appendo x y [:cake :with :ice :d :t])))
;;; => ((:cake :with :ice :d :t)
;;;     (:with :ice :d :t)
;;;     (:ice :d :t)
;;;     (:d :t)
;;;     (:t)
;;;     ())


(run 6 [r]
     (fresh [x y]
            (appendo x y [:cake :with :ice :d :t])
            (== [x y] r)))
;;; => ([() (:cake :with :ice :d :t)]
;;;     [(:cake) (:with :ice :d :t)]
;;;     [(:cake :with) (:ice :d :t)]
;;;     [(:cake :with :ice) (:d :t)]
;;;     [(:cake :with :ice :d) (:t)]
;;;     [(:cake :with :ice :d :t) ()])


(defn appendo
  [l s out]
  (conde
   [(emptyo l) (== s out)]
   [s# (fresh [a d res]
              (conso a d l)
              (conso a res out)
              (appendo d s res))]))


(run 7 [r]
     (fresh [x y]
            (appendo x y [:cake :with :ice :d :t])
            (== [x y] r)))
;;; => ([() (:cake :with :ice :d :t)]
;;;     [(:cake) (:with :ice :d :t)]
;;;     [(:cake :with) (:ice :d :t)]
;;;     [(:cake :with :ice) (:d :t)]
;;;     [(:cake :with :ice :d) (:t)]
;;;     [(:cake :with :ice :d :t) ()])


(run 7 [x]
     (fresh [y z]
            (appendo x y z)))
;;; => (()
;;;     (_0)
;;;     (_0 _1)
;;;     (_0 _1 _2)
;;;     (_0 _1 _2 _3)
;;;     (_0 _1 _2 _3 _4)
;;;     (_0 _1 _2 _3 _4 _5))


(run 7 [y]
     (fresh [x z]
            (appendo x y z)))
;;; (_0 _0 _0 _0 _0 _0 _0)


(run 7 [z]
     (fresh [x y]
            (appendo x y z)))
;;; => (_0
;;;     (_0)
;;;     (_0 . _1)
;;;     (_0 _1 . _2)
;;;     (_0 _1 _2 . _3)
;;;     (_0 _1 _2 _3 . _4)
;;;     (_0 _1 _2 _3 _4 . _5)
;;;     (_0 _1 _2 _3 _4 _5 . _6))


(run 7 [r]
     (fresh [x y z]
            (appendo x y z)
            (== [x y z] r)))
;;; => ([() _0 _0]
;;;     [(_0) _1 (_0 . _1)]
;;;     [(_0 _1) _2 (_0 _1 . _2)]
;;;     [(_0 _1 _2) _3 (_0 _1 _2 . _3)]
;;;     [(_0 _1 _2 _3) _4 (_0 _1 _2 _3 . _4)]
;;;     [(_0 _1 _2 _3 _4) _5 (_0 _1 _2 _3 _4 . _5)]
;;;     [(_0 _1 _2 _3 _4 _5) _6 (_0 _1 _2 _3 _4 _5 . _6)]


(defn swappendo
  [l s out]
  (conde
   [s# (fresh [a d res]
              (conso a d l)
              (conso a res out)
              (swappendo d s res))]
   [(emptyo l) (== s out)]))


(defn unwrap
  [x]
  (if (coll? x)
    (recur (first x))
    x))


(defn unwrapo
  [x out]
  (conde
   [(pairo x) (fresh [a]
                     (firsto x a)
                     (unwrapo a out))]
   [s# (== x out)]))


(run* [x]
      (unwrapo [[[:pizza]]] x))
;;; => ([[[:pizza]]]
;;;     [[:pizza]]
;;;     [:pizza]
;;;     :pizza)


(defn unwrapo
  [x out]
  (conde
   [s# (== x out)]
   [s# (fresh [a]
              (firsto x a)
              (unwrapo a out))]))


(run 5 [x]
     (unwrapo x :pizza))
;;; => (:pizza
;;;     (:pizza . _0)
;;;     ((:pizza . _0) . _1)
;;;     (((:pizza . _0) . _1) . _2)
;;;     ((((:pizza . _0) . _1) . _2) . _3))


(run 5 [x]
     (unwrapo x [[:pizza]]))
;;; => ([[:pizza]]
;;;     ([[:pizza]] . _0)
;;;     (([[:pizza]] . _0) . _1)
;;;     ((([[:pizza]] . _0) . _1) . _2)
;;;     (((([[:pizza]] . _0) . _1) . _2) . _3))


(run 5 [x]
     (unwrapo [[x]] :pizza))
;;; => (:pizza
;;;     (:pizza . _0)
;;;     ((:pizza . _0) . _1)
;;;     (((:pizza . _0) . _1) . _2)
;;;     ((((:pizza . _0) . _1) . _2) . _3)


(defn flatten
  [s]
  (cond (and (coll? s)
             (empty? s))
        ()

        (coll? s)
        (append (flatten (first s))
                (flatten (rest s)))

        :else
        (conj () s)))


(defn flatteno
  [s out]
  (conde
   [(emptyo s) (== out ())]
   [(pairo s) (fresh [a d res-a res-d]
                     (conso a d s)
                     (flatteno a res-a)
                     (flatteno d res-d)
                     (appendo res-a res-d out))]
   [s# (conso s () out)]))

;;; not the same result, core logics conde seems to execute differently
(run 1 [x]
     (flatteno [[:a :b] :c] x))
;;; => (([[:a :b] :c]))


;;; and the same here
(run 1 [x]
     (flatteno [:a [:b :c]] x))
;;; => (([:a [:b :c]]))


(run* [x]
      (flatteno [:a] x))
;;; => (([:a]) (:a) (:a ()))


(run* [x]
      (flatteno [[:a]] x))
;;; => (([[:a]])
;;;     ([:a] ())
;;;     ([:a])
;;;     (:a)
;;;     (:a ())
;;;     (:a ())
;;;     (:a () ()))


(run* [x]
      (flatteno [[[:a]]] x))
;;; = (([[[:a]]])
;;;    ([[:a]])
;;;    ([[:a]] ())
;;;    ([:a])
;;;    ([:a] ())
;;;    ([:a] ())
;;;    ([:a] () ())
;;;    (:a)
;;;    (:a ())
;;;    (:a ())
;;;    (:a () ())
;;;    (:a ())
;;;    (:a () ())
;;;    (:a () ())
;;;    (:a () () ()))


(run* [x]
      (flatteno [[:a :b] :c] x))
;;; => (([[:a :b] :c])
;;;     ([:a :b] (:c))
;;;     ([:a :b] :c)
;;;     (:a (:b) (:c))
;;;     ([:a :b] :c ())
;;;     (:a (:b) :c)
;;;     (:a (:b) :c ())
;;;     (:a :b (:c))
;;;     (:a :b () (:c))
;;;     (:a :b :c)
;;;     (:a :b :c ())
;;;     (:a :b () :c)
;;;     (:a :b () :c ()))


(defn flattenrevo
  [s out]
  (conde
   [s# (conso s () out)]
   [(emptyo s) (== out ())]
   [s# (fresh [a d res-a res-d]
              (conso a d s)
              (flattenrevo a res-a)
              (flattenrevo d res-d)
              (appendo res-a res-d out))]))


(run* [x]
      (flattenrevo [[:a :b] :c] x))
;;; => (([[:a :b] :c])
;;;     ([:a :b] (:c))
;;;     ([:a :b] :c ())
;;;     (:a (:b) (:c))
;;;     ([:a :b] :c)
;;;     (:a (:b) :c ())
;;;     (:a (:b) :c)
;;;     (:a :b () (:c))
;;;     (:a :b () :c ())
;;;     (:a :b (:c))
;;;     (:a :b () :c)
;;;     (:a :b :c ())
;;;     (:a :b :c))


(run 2 [x]
     (flattenrevo x [:a :b :c]))
;;; => ((:a :b . :c)
;;;     ((:a . :b) . :c))


(count
 (run* [x]
       (flattenrevo [[[[:a [[[:b]]] :c]]] :d] x)))
;;; => 574
