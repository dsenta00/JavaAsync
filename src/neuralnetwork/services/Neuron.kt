package neuralnetwork.services

import javaasync.AsyncService
import javaasync.Manager
import neuralnetwork.NeuronRelation
import kotlin.random.Random

open class Neuron(manager: Manager, name: String) : AsyncService(manager, name) {
    private var value = Random.nextFloat()
    var adjacentNeurons = ArrayList<NeuronRelation>()
    var previousNeurons = ArrayList<NeuronRelation>()


}