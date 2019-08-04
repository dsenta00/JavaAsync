package neuralnetwork.services

import javaasync.AsyncService
import javaasync.Manager
import neuralnetwork.NeuronRelation
import kotlin.random.Random

class NeuralNetwork(manager: Manager, name: String) : AsyncService(manager, name) {

    private var inputNeurons = ArrayList<Neuron>()

    init {
        for (i in 0..2) {
            inputNeurons.add(createService(Neuron::class, "input-$i"))
        }

        for (i in 0..16) {
            val hiddenNeuron = createService(Neuron::class, "hidden-0-$i")

            for (inputNeuron in inputNeurons) {
                val calcWeight = Random.nextFloat()

                hiddenNeuron.previousNeurons.add(NeuronRelation().apply {
                    neuron = inputNeuron
                    weight = calcWeight
                })

                inputNeuron.adjacentNeurons.add(NeuronRelation().apply {
                    neuron = hiddenNeuron
                    weight = calcWeight
                })
            }
        }

        for (i in 1..16) {
            for (j in 0..16) {
                val hiddenNeuron = createService(Neuron::class, "hidden-$i-$j")

                for (k in 0..16) {
                    val previousNeuron = getService("hidden-" + (i - 1) + "-$k") as Neuron
                    val calcWeight = Random.nextFloat()

                    hiddenNeuron.previousNeurons.add(NeuronRelation().apply {
                        neuron = previousNeuron
                        weight = calcWeight
                    })

                    previousNeuron.adjacentNeurons.add(NeuronRelation().apply {
                        neuron = hiddenNeuron
                        weight = calcWeight
                    })
                }
            }
        }
    }
}
