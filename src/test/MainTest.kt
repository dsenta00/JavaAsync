package test

import javaasync.Manager
import neuralnetwork.services.NeuralNetwork

/**
 * Main test.
 */
object MainTest {

    /**
     * Main test.
     *
     * @param args - program arguments.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val manager = Manager()

        manager.traceOn()
        manager.secretaryOn()

        //manager.createService(AsyncServiceTest::class).start()
        manager.createService(NeuralNetwork::class).start()
    }
}
