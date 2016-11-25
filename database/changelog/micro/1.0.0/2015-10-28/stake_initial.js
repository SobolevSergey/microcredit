function calculateStake(sum, days, stakeMin, stakeMax) {
    var c = 0.0435275977526034 * Math.pow(1.00000396879854, sum) * Math.pow(0.972552886121762, days);
    var d = Math.round((c)*1000)/1000;
   if (stakeMin==stakeMax){
      d=stakeMin;
   } else {
    if (d < stakeMin)
        d = stakeMin;
    if (d > stakeMax)
        d = stakeMax;
    var s = d * 1000 % 10;
    if (s != 0) {
        if (s == 1 || s == 6)

            d = Math.round((d - 0.001)*1000)/1000;
        if (s == 2 || s == 7)
            d = Math.round((d - 0.002)*1000)/1000;
        if (s == 3 || s == 8)
            d = Math.round((d + 0.002)*1000)/1000;
        if (s == 4 || s == 9)
            d = Math.round((d + 0.001)*1000)/1000;
        ;
    }
  }
  return d;
}