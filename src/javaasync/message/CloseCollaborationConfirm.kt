package javaasync.message

import javaasync.AsyncService

/**
 * Message for confirm to Employee that Collaboration will close.
 */
class CloseCollaborationConfirm(sender: AsyncService) : CompanyMessage(sender)
