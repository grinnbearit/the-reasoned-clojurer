(ns the-reasoned-clojurer.teaching-old-toys-new-tricks
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :refer [run* fresh == firsto lcons resto conso emptyo]]))


(run* [r]
  (fresh [y x]
    (== [x y] r)))
;;; => ([_0 _1])


(run* [r]
  (fresh [v w]
    (== (let [x v
              y w]
          [x y])
        r)))
;;; => ([_0 _1])


;;; `firsto` is the core.logic equivalent to `caro`
(run* [r]
  (firsto [:a :c :o :r :n] r))
;;; => (:a)


(run* [q]
  (firsto [:a :c :o :r :n] :a)
  (== true q))
;;; => (true)


(run* [r]
  (fresh [x y]
    (firsto [r y] x)
    (== :pear x)))
;;; => (:pear)


;;; `conj` doesn't work on any 2 values so core.logic provides `lcons`
(defn caro
  [p a]
  (fresh [d]
    (== (lcons a d) p)))


;;; using `caro` for the rest of this chapter
(run* [r]
  (fresh [x y]
    (caro [:grape :raisin :pear] x)
    (caro [[:a] [:b] [:c]] y)
    (== (lcons x y) r)))
;;; => ((:grape :a))


;;; `resto` is the core.logic equivalent to `cdro`
(run* [r]
  (fresh [v]
    (resto [:a :c :o :r :n] v)
    (caro v r)))
;;; => (:c)


(defn cdro
  [p d]
  (fresh [a]
    (== (lcons a d) p)))


;;; using `cdro` for the rest of this chapter
(run* [r]
  (fresh [x y]
    (cdro [:grape :raisin :pair] x)
    (caro [[:a] [:b] [:c]] y)
    (== (lcons x y) r)))
;;; => (((:raisin :pair) :a))


(run* [q]
  (cdro [:a :c :o :r :n] [:c :o :r :n])
  (== true q))
;;; => (true)


(run* [x]
  (cdro [:c :o :r :n] [x :r :n]))
;;; => (:o)


(run* [l]
  (fresh [x]
    (cdro l [:c :o :r :n])
    (caro l x)
    (== :a x)))
;;; => ((:a :c :o :r :n))


(run* [l]
  (conso [:a :b :c] [:d :e] l))
;;; => (([:a :b :c] :d :e))


(run* [x]
  (conso x [:a :b :c] [:d :a :b :c]))
;;; => (:d)


(run* [r]
  (fresh [x y z]
    (== [:e :a :d x] r)
    (conso y [:a z :c] r)))
;;; => ([:e :a :d :c])


(run* [x]
  (conso x [:a x :c] [:d :a x :c]))
;;; => (:d)


(run* [l]
  (fresh [x]
    (== [:d :a x :c] l)
    (conso x [:a x :c] l)))
;;; => ([:d :a :d :c])


(run* [l]
  (fresh [x]
    (conso x [:a x :c] l)
    (== [:d :a x :c] l)))
;;; => ([:d :a :d :c])


;;; defining `conjo` to avoid shadowing `conso`
(defn conjo
  [d a p]
  (== (lcons a d) p))


;;; using `conjo` for the rest of this chapter
(run* [l]
  (fresh [d x y w s]
    (conjo [:a :n :s] w s)
    (cdro l s)
    (caro l x)
    (== :b x)
    (cdro l d)
    (caro d y)
    (== :e y)))
;;; => ((:b :e :a :n :s))


;;; `emptyo` is the core.logic equivalent to `nullo`
(run* [q]
  (emptyo [:grape :raisin :pear])
  (== true q))
;;; => ()


(run* [q]
  (emptyo [])
  (== true q))
;;; => (true)


(run* [x]
  (emptyo x))
;;; => (())


(defn nullo
  [x]
  (== x ()))


;;; there isn't a need for an `eqo` in core.logic `==` works just as well
(run* [q]
  (== :pear :plum)
  (== true q))
;;; => ()


(run* [q]
  (== :plum :plum)
  (== true q))
;;; => (true)


;;; see
(defn eqo
  [x y]
  (== x y))


(run* [r]
  (fresh [x y]
    (== (lcons x (lcons y :salad)) r)))
;;; => ((_0 _y . salad))


(defn pairo
  [p]
  (fresh [a d]
    (conjo d a p)))

(run* [q]
  (pairo (lcons q q))
  (== true q))
;;; => (true)


(run* [q]
  (pairo ())
  (== true q))
;;; => ()


(run* [q]
  (pairo :pair)
  (== true q))
;;; => ()


(run* [x]
  (pairo x))
;;; => ((_0 . _1))


(run* [r]
  (pairo (lcons r :pear)))
;;; => (_0)
