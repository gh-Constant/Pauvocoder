import static java.lang.System.exit;
import java.util.logging.Logger;

public class Pauvocoder {

    private static final Logger LOGGER = Logger.getLogger(Pauvocoder.class.getName());
    private static final String NOT_IMPLEMENTED = "Not implemented yet";

    // Processing SEQUENCE size (100 msec with 44100Hz samplerate)
    static final int SEQUENCE = StdAudio.SAMPLE_RATE/10;

    // Overlapping size (20 msec)
    static final int OVERLAP = SEQUENCE/5 ;
    // Best OVERLAP offset seeking window (15 msec)
    static final int SEEK_WINDOW = 3*OVERLAP/4;

    public static void main(String[] args) {
        if (args.length < 2)
        {
            LOGGER.severe("usage: pauvocoder <input.wav> <freqScale>");
            exit(1);
        }


        String wavInFile = args[0];
        double freqScale = Double.parseDouble(args[1]);
        String outPutFile= wavInFile.split("\\.")[0] + "_" + freqScale +"_";

        // Open input .wev file
        double[] inputWav = StdAudio.read(wavInFile);

        // Resample test
        double[] newPitchWav = resample(inputWav, freqScale);
        StdAudio.save(outPutFile+"Resampled.wav", newPitchWav);

        // Simple dilatation
        double[] outputWav   = vocodeSimple(newPitchWav, 1.0/freqScale);
        StdAudio.save(outPutFile+"Simple.wav", outputWav);

        // Simple dilatation with overlaping
        outputWav = vocodeSimpleOver(newPitchWav, 1.0/freqScale);
        StdAudio.save(outPutFile+"SimpleOver.wav", outputWav);

        // Simple dilatation with overlaping and maximum cross correlation search
        outputWav = vocodeSimpleOverCross(newPitchWav, 1.0/freqScale);
        StdAudio.save(outPutFile+"SimpleOverCross.wav", outputWav);

        joue(outputWav);

        // Some echo above all
        outputWav = echo(outputWav, 100, 0.7);
        StdAudio.save(outPutFile+"SimpleOverCrossEcho.wav", outputWav);

        // Display waveform
        displayWaveform(outputWav);

    }

    /**
     * Resample inputWav with freqScale
     * @param inputWav
     * @param freqScale
     * @return resampled wav
     */
    public static double[] resample(double[] inputWav, double freqScale) {
        int newSize = (int)(inputWav.length / freqScale);
        double[] output = new double[newSize];
        
        if (freqScale > 1) {
            // Sous-échantillonnage
            for (int i = 0; i < newSize; i++) {
                int pos = (int)(i * freqScale);
                if (pos < inputWav.length) {
                    output[i] = inputWav[pos];
                }
            }
        } else {
            // Sur-échantillonnage
            for (int i = 0; i < newSize; i++) {
                double exactPos = i * freqScale;
                int pos = (int)exactPos;
                
                if (pos >= inputWav.length - 1) {
                    output[i] = inputWav[inputWav.length - 1];
                    continue;
                }
                
                double ratio = exactPos - pos;
                output[i] = inputWav[pos] * (1.0 - ratio) + inputWav[pos + 1] * ratio;
            }
        }
        
        return output;
    }

    /**
     * Simple dilatation, without any overlapping
     * @param inputWav
     * @param dilatation factor
     * @return dilated wav
     */
    public static double[] vocodeSimple(double[] inputWav, double dilatation) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Simple dilatation, with overlapping
     * @param inputWav
     * @param dilatation factor
     * @return dilated wav
     */
    public static double[] vocodeSimpleOver(double[] inputWav, double dilatation) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Simple dilatation, with overlapping and maximum cross correlation search
     * @param inputWav
     * @param dilatation factor
     * @return dilated wav
     */
    public static double[] vocodeSimpleOverCross(double[] inputWav, double dilatation) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Play the wav
     * @param wav
     */
    public static void joue(double[] wav) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Add an echo to the wav
     * @param wav
     * @param delay in msec
     * @param gain
     * @return wav with echo
     */

    /**
     * Display the waveform
     * @param wav
     */
    public static void displayWaveform(double[] wav) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }


}
