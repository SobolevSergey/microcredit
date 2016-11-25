if (!decisionState.nextLabel) {
    decisionState.start();

    //decisionState.externalSystems.get('plugin_verify').needCall = true;
    //verify.question('client.marry').add();
    //verify.question('client.work').add();
    //verify.question('age.verify').add();
    //verify.question('salary.verify').add();

    //identity.createQuestions();

    decisionState.nextLabel = 'label_check';
} else if (decisionState.nextLabel == 'label_check') {

    var ncredits = 0;

    if (creditRequest.hasCredits > 0) {
        for (i = 0; i < creditRequest.credits.size(); i++) {
            if (creditRequest.credits.get(i).partner.id != 6) {

                ncredits++;
            }
        }
    }

    decisionState.vars.put('ncredits', ncredits);

    var ver = 0;

    if (creditRequest.hasVerif > 0) {
        for (i = 0; i < creditRequest.verif.size(); i++) {

            ver++;
        }
    }

    decisionState.vars.put('ver', ver);

    decisionState.nextLabel = 'label_finish';
} else if (decisionState.nextLabel == 'label_finish') {
    decisionState.finish(true, MConst.decision.RESULT_ACCEPT);
}