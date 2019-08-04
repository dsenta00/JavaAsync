package javaasync.message

import javaasync.AsyncService

/**
 * Dummy message for notifying another employees for job quit.
 */
class CloseCollaborationRequest(sender: AsyncService) : CompanyMessage(sender)
