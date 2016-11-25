if (! decisionState.nextLabel) {
	decisionState.start();
	decisionState.externalSystems.get('plugin_rusStandart').needCall = false;
	decisionState.externalSystems.get('plugin_verify').needCall = true;
	verify.question('age.verify').add();
	verify.question('credit.last').add();
	decisionState.nextLabel = 'label_finish';
} else if (decisionState.nextLabel == 'label_finish') {	
	decisionState.finish(true, MConst.decision.RESULT_ACCEPT);
}