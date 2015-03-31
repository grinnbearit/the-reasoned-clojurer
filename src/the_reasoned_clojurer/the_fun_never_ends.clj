(ns the-reasoned-clojurer.the-fun-never-ends
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :refer [conde s# u# run == all]]
            [the-reasoned-clojurer.playthings :refer [teacupo]]))


(defn anyo
  [g]
  (conde
   [g s#]
   [s# (anyo g)]))


(def nevero
  (anyo u#))


(run 1 [q]
  u#
  nevero)
;;; => ()


(def alwayso
  (anyo s#))

(run 1 [q]
  alwayso
  (== true q))
;; => (true)


(run 5 [q]
  alwayso
  (== true q))
;;; => (true
;;;     true
;;;     true
;;;     true
;;;     true)


(defn salo
  [g]
  (conde
   [s# s#]
   [s# g]))


(run 1 [q]
  (salo alwayso)
  (== true q))
;;; => (true)


(run 1 [q]
  (salo nevero)
  (== true q))
;;; => (true)


;;; conde is evaluated differently in core.logic
;;; I think the second branch head is tried before
;;; `alwayso` is tried again
;;;
;;; (run 1 [q]
;;;   (conde
;;;    [(== false q) alwayso]
;;;    [s# u#])
;;;   (== true q))
;;;
;;; has no value


(run 1 [q]
  (conde
   [(== false q) alwayso]
   [s# (anyo (== true q))])
  (== true q))
;;; => (true)


;;; https://github.com/clojure/core.logic/wiki/Differences-from-The-Reasoned-Schemer
;;; I have just realised that core logic's `conde` is actually `condi`
(run 1 [q]
  (conde
   [(== false q) alwayso]
   [s# (== true q)])
  (== true q))
;;; => (true)


(run 5 [q]
  (conde
   [(== false q) alwayso]
   [s# (anyo (== true q))])
  (== true q))
;;; => (true
;;;     true
;;;     true
;;;     true
;;;     true)


;;; core.logic apparently reorders clauses
(run 5 [r]
  (conde
   [(teacupo r) s#]
   [(== false r) s#]
   [s# u#]))
;;; => (false
;;;     :tea
;;;     :cup)


(run 5 [q]
  (conde
   [(== false q) alwayso]
   [(== true q) alwayso]
   [s# u#])
  (== true q))
;;;  => (true
;;;      true
;;;      true
;;;      true
;;;      true)


;;; here, `conde` doesn't evaluate `nevero` continuously
;;; I think `conde` interleaves both branches
;;;
;;; (run 4 [q]
;;;   (conde
;;;    [s# (anyo (== 1 q))]
;;;    [s# (anyo (== 2 q))]))
;;; => (1 2 1 2)
;;;
;;; here, `nevero` is probably called 5 times as well but doesn't return a result
(run 5 [q]
  (conde
   [alwayso s#]
   [s# nevero])
  (== true q))
;;;  => (true
;;;      true
;;;      true
;;;      true
;;;      true)


;;; succeeds, see above
(run 1 [q]
  (all
   (conde
    [(== false q) s#]
    [s# (== true q)])
   alwayso)
  (== true q))
;;; => (true)


(run 5 [q]
  (all
   (conde
    [(== false q) s#]
    [s# (== true q)])
   alwayso)
  (== true q))
;;; => (true
;;;     true
;;;     true
;;;     true
;;;     true)


(run 5 [q]
  (all
   (conde
    [s# (== true q)]
    [(== false q) s#])
   alwayso)
  (== true q))
;;; => (true
;;;     true
;;;     true
;;;     true
;;;     true)

;;; core.logic seems to always interleave


(run 5 [q]
  (all
   (conde
    [s# s#]
    [s# nevero])
   alwayso)
  (== true q))
;;; => (true
;;;     true
;;;     true
;;;     true
;;;     true)
