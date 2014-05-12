# henzeZirklerMultivariateNormalityTest.praat
# djmw 20090707,20110526

printline test_henzeZirklerMultivariateNormalityTest
ir = Create iris data set
ir50 = Extract row ranges...  1:50
report$ = Report multivariate normality... 0
tol = 1e-10
stat = extractNumber (report$, "statistic:")
assert abs(stat - 0.958366538697967)<tol
prob = extractNumber (report$, " normality:")
assert abs (prob - 0.04292525730817163) < tol
lmean = extractNumber (report$, " mean:")
assert abs (lmean  + 0.27940830765481794)<tol
lvariance = extractNumber (report$, " variance:")
assert abs (lvariance - 0.13790691675095398)<tol
beta = extractNumber (report$, "Smoothing:")
printline 'beta'
assert abs (beta -0.5541226910830553)<tol
sampleSize = extractNumber (report$, " size:")
assert sampleSize = 50
nvars = extractNumber (report$, " variables:")
assert nvars = 4

select ir
plus ir50
Remove

printline henzeZirklerMultivariateNormalityTest : end
