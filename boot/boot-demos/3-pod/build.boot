(set-env!
 :source-paths #{"src" "task"})

(require '[boot.pod :as pod])

(defn- lc-files [fileset]
  (->> fileset
       input-files
       (by-ext [".lc"])))

(defn lc->uc-path [path]
  (clojure.string/replace path #".lc$" ".uc"))

(defn lc->uc [pod dir files]
  (doseq [f files
          :let [in-file (tmp-file f)
                rel-path (tmp-path f)]]
    (info "Upcasing %s...\n" rel-path)
    (pod/with-call-in pod
      (alandipert.upcase/upcase!
       ~(.getPath in-file)
       ~(.getPath dir)
       ~(lc->uc-path rel-path)))))

(deftask upper
  []
  (let [tmp (tmp-dir!)
        pod (pod/make-pod (update-in (get-env) [:dependencies] conj '[alandipert/upcase "2.0.0"]))]
    (with-pre-wrap fileset
      (empty-dir! tmp)
      (lc->uc pod tmp (lc-files fileset))
      (commit! (add-resource fileset tmp)))))
