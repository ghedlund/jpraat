# test_KruskalWallis.praat

printline One-way Kruskal-Wallis test
table = Read from file... Hayes_table_19.7.1.txt
report$ = Report one-way Kruskal-Wallis... Data Group
hprime = extractNumber (report$, "Chi squared:")
groupI = extractNumber (report$, " I")
groupII = extractNumber (report$, " II")
groupIII = extractNumber (report$, " III")
printline 'hprime' 'groupI' 'groupII' 'groupIII'
assert abs(hprime-13.8443) < 0.0001
assert groupI = 139
assert groupII = 200
assert groupIII = 327
printline One-way Kruskal-Wallis test OK


