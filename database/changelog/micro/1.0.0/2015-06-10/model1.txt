if (! decisionState.nextLabel) {
	decisionState.start();
	decisionState.nextLabel = 'label_finish';
} else if (decisionState.nextLabel == 'label_finish') {	
	decisionState.finish(true, MConst.decision.RESULT_ACCEPT);
}