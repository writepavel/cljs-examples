(ns baflo.tic-tac-toe.main 
  (:require ["readline" :as readline]))

(def side-length 3)
(def field-count (* side-length side-length))

(defn create-game []
  (atom {:field (vec (repeat (* side-length side-length) :empty))
         :current-user :circle}))

(defn next-user "Expects a game-state gs" [gs]
  (swap! gs #(assoc %1 :current-user ((:current-user @gs) {:circle :cross :cross :circle}))))

(defn get-fields [field indices]
  (map field indices))
(defn anti-diagonal [field]
  (map #(nth field %) (range 0 (* side-length side-length) (inc side-length))))
(defn diagonal [field]
  (map #(nth field %) (range (dec side-length) (inc (* side-length (dec side-length))) (dec side-length))))
(defn rows [field]
  (partition side-length side-length field))
(defn cols [field]
  (for [colIndex (range side-length)] (flatten (partition 1 side-length (drop colIndex field)))))

(def diagonals (juxt diagonal anti-diagonal))
(defn all-strikes [field]
  (reduce concat ((juxt diagonals rows cols) field)))

(defn won? [gs]
  (let [{user :current-user field :field} @gs]
    (true? (some #(= (repeat side-length user) %) (all-strikes field)))))

(defn draw? [gs]
  (let [{field :field} @gs]
    (and (not (won? gs)) (->> field
                              (all-strikes)
                              (flatten)
                              (#((set %2) %1) :empty)
                              (not)))))

(defn empty-field? [gs idx]
  (= :empty ((:field @gs) idx :not-empty)))

(defn play "Expects game-state gs" [gs idx]
  (next-user gs)
  (let [{field :field
         user :current-user} @gs]
    (swap! gs #(assoc %1 :field (assoc field idx user)))))


(.emitKeypressEvents readline js/process.stdin)
(.setRawMode js/process.stdin true)

(defn empty-field [n] ["           "
                       "           "
                       (str "     " (inc n) "     ")
                       "           "
                       "           "])
(defn cross-field [n] ["8b,     ,d8"
                       " `Y8, ,8P' "
                       "   )888(   "
                       " ,d8\" \"8b, "
                       "8P'     `Y8"])
(defn circle-field [n] ["    ooo    "
                        " o       o "
                        "o         o"
                        " o       o "
                        "    ooo    "])

(defn print-field [field]
  (->> field
       (map-indexed #(({:empty empty-field :cross cross-field :circle circle-field} %2) %1)) ;; Take ascii images for fields
       (apply interleave)         ;; make all first ascii-lines come first, all second ascii-lines come second... 
       (partition side-length)    ;; group every side-len fields that correspond to one tic-tac-toe-row
       (map #(apply str %))       ;; concat every partition to a single string -> a single ascii-line
       (partition side-length)    ;; group first lines to first ascii-lines, second lines to second lines, etc
       (apply interleave)         ;; re-group so all ascii-lines of one tic-tac-toe row are sequential
       (clojure.string/join "\n")
       (println)))

(defn reload! []
  (println "App reloaded!"))

(defn main! []
  (let [game-state (create-game)]
    (println "Tic Tac Toe")
    (print-field (:field @game-state))
    (.on js/process.stdin "keypress" #(let [field-nr (dec (js/parseInt %1))]
                                        (when (empty-field? game-state field-nr)
                                          (play game-state field-nr)
                                          (println "--------------")
                                          (print-field (:field @game-state))
                                          (when (or (won? game-state) (draw? game-state))
                                            (js/process.exit)))))))
