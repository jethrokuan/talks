(set-env!
 :source-paths #{"src"})

(deftask foo
  "Foo Task"
  [a arg ARGVAL int "the argument"]
  (let [state (atom arg)]
    (with-pre-wrap fileset
      (println "hello clojurians!"
               (swap! state inc))      
      fileset)))

(deftask bar
  "Comp foo task"
  []
  (comp
   (foo :arg 5)
   (foo :arg 10)))
