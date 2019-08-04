package neuralnetwork

import neuralnetwork.services.Neuron
import kotlin.random.Random

class NeuronRelation {
    lateinit var neuron: Neuron
    var weight = Random.nextFloat()
}