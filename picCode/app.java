//Funcion para girar la flecha del sensor de temperatura
  public void rotateTemperature(){
        long delay = 798;
        float pI,  pF;
        pI = 360.00f*act/1024.00f;
        pF = 350.00f*prox/1024.00f;
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(rotCircle, "rotation",  pI, pF);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(delay);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnimation);
        animatorSet.start();

        rotateAnimation = ObjectAnimator.ofFloat(rotCircle, "rotation",  pF, pF);
        rotateAnimation.setDuration(1);
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnimation);
        animatorSet.start();

    }